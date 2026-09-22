package com.onebx.ebx.fasttrack.referencedata;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationReference;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.SchemaNode;
import com.orchestranetworks.service.LoggingCategory;
import com.orchestranetworks.service.Procedure;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.ProcedureResult;
import com.orchestranetworks.service.Profile;
import com.orchestranetworks.service.ProgrammaticService;
import com.orchestranetworks.service.Session;
import com.orchestranetworks.service.ValueContextForUpdate;

/** Creates a focused Reference Data perspective once and preserves later administrator edits. */
final class ReferenceDataPerspectiveInstaller {
    static final String KEY = "ebx-perspective-reference-data";
    private static final HomeKey MANAGER_HOME = HomeKey.forBranchName("ebx-manager");
    private static final AdaptationName MANAGER_ROOT = AdaptationName.forName("ebx-manager");
    private static final Path MENU = Path.parse("/domain/menuItem");
    private static final Path ACTIVATED = Path.parse("/domain/properties/activated");
    private static final Path DEFAULT_SELECTION = Path.parse("/domain/properties/defaultSelection");

    private ReferenceDataPerspectiveInstaller() { }

    static boolean install(final Repository repository, final Session session, final LoggingCategory log) {
        if ("false".equalsIgnoreCase(System.getProperty("ebx.referenceData.perspective", "true"))) {
            log.info("[reference-data] perspective installation disabled");
            return true;
        }
        final AdaptationHome home = repository.lookupHome(MANAGER_HOME);
        final Adaptation parent = home == null ? null : home.findAdaptationOrNull(MANAGER_ROOT);
        if (parent == null) {
            log.warn("[reference-data] perspective not installed: ebx-manager is unavailable");
            return false;
        }
        if (home.findAdaptationOrNull(AdaptationName.forName(KEY)) != null) {
            log.info("[reference-data] perspective " + KEY + " already present, left unchanged");
            return true;
        }
        final String[] defaultId = { null };
        final ProcedureResult result = ProgrammaticService.createForSession(session, home).execute(new Procedure() {
            @Override
            public void execute(final ProcedureContext context) throws Exception {
                context.setAllPrivileges(true);
                final Adaptation child = context.doCreateChild(
                    parent.getAdaptationName(), AdaptationReference.forPersistentName(KEY), Profile.ADMINISTRATOR);
                context.setInstanceLabel(child, UserMessage.createInfo("Reference Data"));
                final AdaptationTable menu = child.getTable(MENU);
                final String section = createMenuItem(context, menu, "section", null, 1000,
                    "Reference Data", "/" + ModuleNames.REFERENCE_DATA + "/www/common/images/icons/svg/reference.svg", null);
                int order = 1000;
                for (final String[] definition : ReferenceDataFoundationInstaller.DATASETS) {
                    final String id = createMenuItem(context, menu, "action", section, order++,
                        definition[2], "/" + ModuleNames.REFERENCE_DATA + "/www/common/images/icons/svg/reference.svg",
                        definition);
                    if (defaultId[0] == null) defaultId[0] = id;
                }
                final ValueContextForUpdate properties = context.getContext(child.getAdaptationName());
                properties.setValue(Boolean.TRUE, ACTIVATED);
                properties.setValue(defaultId[0], DEFAULT_SELECTION);
                context.doModifyContent(child, properties);
            }
        });
        if (result.hasFailed()) {
            log.error("[reference-data] perspective not installed: "
                + result.getExceptionFullMessage(Locale.ENGLISH));
            return false;
        } else {
            log.info("[reference-data] perspective " + KEY + " installed and activated");
        }
        return true;
    }

    private static String createMenuItem(final ProcedureContext context, final AdaptationTable table,
        final String type, final String parent, final int order, final String label,
        final String icon, final String[] dataset) throws Exception {
        final ValueContextForUpdate row = context.getContextForNewOccurrence(table);
        row.setValue(type, Path.parse("type"));
        if (parent != null) row.setValue(parent, Path.parse("parent"));
        row.setValue(Integer.valueOf(order), Path.parse("order"));
        row.setValue(Boolean.FALSE, Path.parse("hasTopSeparator"));
        row.setValue("url", Path.parse("icon/type"));
        row.setValue(icon, Path.parse("icon/reference"));

        final SchemaNode docsNode = row.getNode(Path.parse("label/localizedDocumentations"));
        final Object documentation = docsNode.createNewOccurrence();
        docsNode.getNode(Path.parse("locale")).executeWrite(Locale.forLanguageTag("en-US"), documentation);
        docsNode.getNode(Path.parse("label")).executeWrite(label, documentation);
        final List<Object> documentations = new ArrayList<Object>();
        documentations.add(documentation);
        row.setValue(documentations, Path.parse("label/localizedDocumentations"));

        if (dataset != null) {
            row.setValue("S", Path.parse("action/type"));
            row.setValue("ebx-root-1.0@default", Path.parse("action/serviceKey"));
            final SchemaNode paramsNode = row.getNode(Path.parse("action/serviceParameters"));
            final List<Object> params = new ArrayList<Object>();
            params.add(parameter(paramsNode, "service", "@default"));
            params.add(parameter(paramsNode, "branch", dataset[0]));
            params.add(parameter(paramsNode, "instance", dataset[0]));
            params.add(parameter(paramsNode, "xpath", dataset[3]));
            row.setValue(params, Path.parse("action/serviceParameters"));
        }
        final Adaptation created = context.doCreateOccurrence(row, table);
        return String.valueOf(((Number) created.get(Path.parse("id"))).intValue());
    }

    private static Object parameter(final SchemaNode group, final String name, final String value) {
        final Object occurrence = group.createNewOccurrence();
        group.getNode(Path.parse("name")).executeWrite(name, occurrence);
        group.getNode(Path.parse("value")).executeWrite(value, occurrence);
        return occurrence;
    }
}
