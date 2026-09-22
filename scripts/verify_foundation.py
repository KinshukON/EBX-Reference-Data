#!/usr/bin/env python3
"""Check foundation navigation and seed values against the actual packaged XSDs.

Static contract check only; repository startup and EBX validation remain runtime checks.
"""
import pathlib
import re
import xml.etree.ElementTree as ET
import zipfile

ROOT = pathlib.Path(__file__).resolve().parents[1]
WEB = ROOT / "referencedata-web"
WEBAPP = WEB / "src/main/webapp"
SCHEMAS = WEBAPP / "WEB-INF/ebx/schemas"
JAVA = WEB / "src/main/java/com/onebx/ebx/fasttrack/referencedata"
NS = {"xs": "http://www.w3.org/2001/XMLSchema", "osd": "urn:ebx-schemas:common_1.0"}
source = (JAVA / "ReferenceDataFoundationInstaller.java").read_text()
registry = re.findall(r'\{"([^"]+)", "([^"]+\.xsd)", "([^"]+)", "([^"]+)"\}', source)
assert len(registry) == 5, "Expected five foundation definitions"
assert len({item[0] for item in registry}) == len(registry), "Duplicate dataspace"
tables_by_dataset = {}
for branch, filename, label, landing in registry:
    assert len(branch) <= 32, f"Dataspace name too long: {branch}"
    root = ET.parse(SCHEMAS / filename)
    tables = {
        "/root/" + node.attrib["name"]: node
        for node in root.findall("./xs:element/xs:complexType/xs:sequence/xs:element", NS)
        if node.find("./xs:annotation/xs:appinfo/osd:table", NS) is not None
    }
    assert landing in tables, f"Missing landing table: {branch} {landing}"
    tables_by_dataset[branch] = tables

required_tables = {
    "CommonReferenceData": ("SourceSystem", "ExternalSystemType", "IdentifierType", "AddressType",
                            "ContactType", "DocumentType", "LifecycleStatus", "ApprovalStatus"),
    "Geographies": ("Country", "BusinessRegion"), "UOM": ("UOM",),
    "Currencies": ("Currency",), "Languages": ("Language",),
}
for dataset, names in required_tables.items():
    for name in names:
        assert "/root/" + name in tables_by_dataset[dataset], f"Missing contract table: {dataset}/{name}"

seeds = re.findall(r'seed\(context, dataset, "([^"]+)", "([^"]+)", "([^"]+)", "([^"]+)", "([^"]+)"\);', source)
assert seeds, "No source registry seeds found"
seed_keys = {(name, code) for name, code, *_ in seeds}
assert len(seed_keys) == len(seeds), "Duplicate seed key"
for name, code, label, category_field, category in seeds:
    table = tables_by_dataset["CommonReferenceData"]["/root/" + name]
    primary_keys = table.find("./xs:annotation/xs:appinfo/osd:table/primaryKeys", NS).text.split()
    assert primary_keys == ["/code"], f"Seed key incompatible with {name}: {primary_keys}"
    fields = {node.attrib["name"]: node for node in table.findall("./xs:complexType/xs:sequence/xs:element", NS)}
    values = {"code": code, "label": label, "status": "Active", category_field: category}
    for field_name, value in values.items():
        assert field_name in fields, f"Unknown seed field: {name}/{field_name}"
        field = fields[field_name]
        enum = [node.attrib["value"] for node in field.findall("./xs:simpleType/xs:restriction/xs:enumeration", NS)]
        assert not enum or value in enum, f"Invalid enumeration: {name}/{field_name}={value}"
        target = field.find("./xs:annotation/xs:appinfo/osd:tableRef/tablePath", NS)
        if target is not None:
            assert (target.text.rsplit("/", 1)[-1], value) in seed_keys, f"Unseeded FK: {name}/{field_name}={value}"
    for field_name, field in fields.items():
        if field.get("minOccurs", "1") != "0" and "default" not in field.attrib:
            assert field_name in values, f"Missing required seed field: {name}/{field_name}"

perspective = (JAVA / "ReferenceDataPerspectiveInstaller.java").read_text()
icons = set(re.findall(r'"(/www/[^"\n]+\.svg)"', perspective))
assert icons, "No perspective icons found"
for icon in icons:
    assert (WEBAPP / icon.lstrip("/")).is_file(), f"Missing icon: {icon}"
assert "localhost:3001" not in perspective
assert '"ebx-root-1.0@default"' in perspective
assert "ReferenceDataFoundationInstaller.DATASETS" in perspective
assert "doDelete(" not in source and "deleteHome(" not in source
module_name = ET.parse(WEBAPP / "WEB-INF/ebx/module.xml").findtext("./{*}name")
assert module_name == "ebx-reference-data", f"Wrong module identity: {module_name}"

war = WEB / "target/ebx-reference-data.war"
if war.exists():
    with zipfile.ZipFile(war) as archive:
        for relative in ["WEB-INF/ebx/schemas/" + row[1] for row in registry] + [icon.lstrip("/") for icon in icons]:
            assert archive.read(relative) == (WEBAPP / relative).read_bytes(), f"Stale/missing WAR resource: {relative}"
print(f"PASS: {len(registry)} foundations, {len(seeds)} seed records, key/required-field/enum/FK checks, {len(icons)} icon(s)")
