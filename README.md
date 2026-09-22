# EBX Reference Data

Reference-data management module for ON EBX FastTrack.

## Modules

- `referencedata-lib`: reusable Java services and components.
- `referencedata-web`: EBX module packaging, schemas, web resources, and reference datasets.

## Build

The workspace build requires JDK 21 or later because its EBX library uses Java 21
bytecode, and an EBX server library directory containing `ebx-lib.jar`. Maven
retains `release=17` for this module's source/bytecode target; that setting does
not make the EBX runtime library compatible with a Java 17 compiler or runtime.

```bash
export EBX_LIB_DIR=/path/to/ebx-server/lib
mvn clean package
python3 scripts/verify_foundation.py
```

The project can also be included as the `EBX-Reference-Data` module in the ON EBX FastTrack Maven reactor.

## Automatic foundation and perspective

Repository startup creates a `ReferenceData` parent dataspace and the following
child dataspace/dataset pairs. Existing datasets and records are retained.

| Dataspace and dataset | Schema | Initial native table |
| --- | --- | --- |
| CommonReferenceData | CommonReferenceData.xsd | /root/SourceSystem |
| Geographies | Geography.xsd | /root/Country |
| UOM | UOM.xsd | /root/UOM |
| Currencies | Currency.xsd | /root/Currency |
| Languages | Language.xsd | /root/Language |

The `ebx-perspective-reference-data` perspective is created and activated after
the foundation is available. It opens native EBX tables rather than the optional
external Reference Data Hub. Existing administrator-edited perspectives are
preserved. Set `ebx.referenceData.perspective=false` to disable perspective creation.

Bootstrap attempts foundation provisioning followed by perspective installation.
If a repository transaction fails or the perspective manager is not ready, it
retries every 30 seconds, up to 10 attempts including the initial attempt.
Committed datasets and seed records are reused on retry. Exhaustion is logged
as an error with instructions to resolve the cause and restart; a failed attempt
is never reported as a completed installation.

Startup inserts only the locally defined `ERP` system type and `ECC_NA` source
system needed by the Manufacturing demo, when absent. It does not overwrite
governed records or claim to load full ISO, healthcare, or industry code lists.
Those authoritative data feeds remain separate work.

Compatible existing schemas under an older module are retained with a warning;
missing contract tables fail installation explicitly. No dataset is deleted or
automatically migrated. Deploy this module before Manufacturing. The current
workspace build is verified against EBX `6.3.0.1-DEV-SNAPSHOT`, with Java 17
source compatibility.

The static verification checks foundation table targets, seed field names,
primary keys, required values, enumerations and seed foreign keys against the
actual XSD files, plus perspective icons and packaged WAR resources when built.
Repository startup and permission behavior still require runtime verification.
