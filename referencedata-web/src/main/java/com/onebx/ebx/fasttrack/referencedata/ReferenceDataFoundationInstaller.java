package com.onebx.ebx.fasttrack.referencedata;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationReference;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.HomeCreationSpec;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.SchemaLocation;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.LoggingCategory;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ProcedureResult;
import com.orchestranetworks.service.Profile;
import com.orchestranetworks.service.ProgrammaticService;
import com.orchestranetworks.service.Session;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.ValueContextForUpdate;

/** Provisions the published cross-module foundation contract without changing existing data. */
final class ReferenceDataFoundationInstaller {
    static final String[][] DATASETS = {
        {"CommonReferenceData", "CommonReferenceData.xsd", "Common reference data", "/root/SourceSystem"},
        {"Geographies", "Geography.xsd", "Geographies", "/root/Country"},
        {"UOM", "UOM.xsd", "Units of measure", "/root/UOM"},
        {"Currencies", "Currency.xsd", "Currencies", "/root/Currency"},
        {"Languages", "Language.xsd", "Languages", "/root/Language"}
    };

    private ReferenceDataFoundationInstaller() { }

    static void install(final Repository repository, final Session session, final LoggingCategory log)
        throws OperationException {
        final AdaptationHome parent = ensureHome(repository, session, "ReferenceData",
            "Reference Data", repository.getReferenceBranch());
        for (final String[] definition : DATASETS) {
            final AdaptationHome home = ensureHome(repository, session, definition[0], definition[2], parent);
            final AdaptationReference name = AdaptationReference.forPersistentName(definition[0]);
            final String expected = ModuleNames.schema(definition[1]);
            final Adaptation existing = home.findAdaptationOrNull(name);
            if (existing != null) {
                verifyContract(existing, definition[0]);
                if (!expected.equals(existing.getSchemaLocation().format())) {
                    log.warn("[reference-data] preserving compatible existing dataset " + definition[0]
                        + " with schema " + existing.getSchemaLocation().format()
                        + "; new deployments use " + expected);
                }
                continue;
            }
            final ProcedureResult result = ProgrammaticService.createForSession(session, home).execute(context -> {
                context.setAllPrivileges(true);
                final Adaptation dataset = context.doCreateRoot(SchemaLocation.parse(expected), name,
                    Profile.ADMINISTRATOR);
                context.setInstanceLabel(dataset, UserMessage.createInfo(definition[2]));
            });
            if (result.hasFailed()) throw result.getException();
            log.info("[reference-data] provisioned " + definition[0] + "/" + definition[0]);
        }
        seedInternalSourceRegistry(repository, session);
    }

    private static void verifyContract(final Adaptation dataset, final String name) throws OperationException {
        final String[] tables;
        if ("CommonReferenceData".equals(name)) {
            tables = new String[] {"SourceSystem", "ExternalSystemType", "IdentifierType", "AddressType",
                "ContactType", "DocumentType", "LifecycleStatus", "ApprovalStatus"};
        } else if ("Geographies".equals(name)) {
            tables = new String[] {"Country", "BusinessRegion"};
        } else {
            tables = new String[] {"Currencies".equals(name) ? "Currency" : "Languages".equals(name) ? "Language" : "UOM"};
        }
        for (final String table : tables) {
            if (dataset.getSchemaNode().getNode(Path.parse("/root/" + table)) == null) {
                throw OperationException.createError("Existing dataset " + name + " is missing required table /root/"
                    + table + ". Existing data has been preserved; migrate its schema explicitly.");
            }
        }
    }

    /** Local configuration records, not a substitute for authoritative external code lists. */
    private static void seedInternalSourceRegistry(final Repository repository, final Session session)
        throws OperationException {
        final AdaptationHome home = repository.lookupHome(HomeKey.forBranchName("CommonReferenceData"));
        final Adaptation dataset = home.findAdaptationOrNull(AdaptationReference.forPersistentName("CommonReferenceData"));
        final ProcedureResult result = ProgrammaticService.createForSession(session, home).execute(context -> {
            context.setAllPrivileges(true);
            seed(context, dataset, "ExternalSystemType", "ERP", "Enterprise resource planning", "systemCategory", "Enterprise");
            seed(context, dataset, "SourceSystem", "ECC_NA", "SAP ECC North America", "systemType", "ERP");
        });
        if (result.hasFailed()) throw result.getException();
    }

    private static void seed(final ProcedureContext context, final Adaptation dataset, final String tableName,
        final String code, final String label, final String categoryField, final String category) throws Exception {
        final AdaptationTable table = dataset.getTable(Path.parse("/root/" + tableName));
        if (table.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(code)) != null) return;
        final ValueContextForUpdate row = context.getContextForNewOccurrence(table);
        row.setValue(code, Path.parse("code"));
        row.setValue(label, Path.parse("label"));
        row.setValue("Active", Path.parse("status"));
        row.setValue(category, Path.parse(categoryField));
        context.doCreateOccurrence(row, table);
    }

    private static AdaptationHome ensureHome(final Repository repository, final Session session,
        final String key, final String label, final AdaptationHome parent) throws OperationException {
        final AdaptationHome existing = repository.lookupHome(HomeKey.forBranchName(key));
        if (existing != null) return existing;
        final HomeCreationSpec specification = new HomeCreationSpec();
        specification.setKey(HomeKey.forBranchName(key));
        specification.setLabel(UserMessage.createInfo(label));
        specification.setOwner(Profile.ADMINISTRATOR);
        specification.setParent(parent);
        specification.setHomeToCopyPermissionsFrom(parent);
        return repository.createHome(specification, session);
    }
}
