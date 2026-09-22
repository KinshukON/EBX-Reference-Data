package com.onebx.ebx.fasttrack.referencedata;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.ServiceKey;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.schema.types.dataspace.DataspaceSet;
import com.orchestranetworks.userservice.declaration.ActivationContextOnTableView;
import com.orchestranetworks.userservice.declaration.UserServiceDeclaration;
import com.orchestranetworks.userservice.declaration.UserServicePropertiesDefinitionContext;
import com.orchestranetworks.userservice.declaration.WebComponentDeclarationContext;

public class FetchNuccDataUserServiceDeclaration implements UserServiceDeclaration.OnTableView {

    public static final ServiceKey SERVICE_KEY = ServiceKey.forName("EBX Reference Data Module@FetchNuccDataService");

    @Override
    public ServiceKey getServiceKey() {
        return SERVICE_KEY;
    }

    @Override
    public UserService<TableViewEntitySelection> createUserService() {
        return new FetchNuccDataUserService();
    }

    @Override
    public void defineActivation(ActivationContextOnTableView aContext) {
        aContext.includeAllDataspaces(DataspaceSet.DataspaceType.ALL);
        aContext.includeAllDatasets(); // CRITICAL: Without this, it activates on NO datasets!
        // Restrict this service to only appear on the 'Nucc' table
        aContext.includeSchemaNodesMatching(Path.parse("/root/nucc"));
    }

    @Override
    public void defineProperties(UserServicePropertiesDefinitionContext aContext) {
        aContext.setLabel("Fetch Latest NUCC Data");
        aContext.setDescription("Fetches the latest taxonomy data from the configured endpoint and updates the table.");
    }

    @Override
    public void declareWebComponent(WebComponentDeclarationContext aContext) {
        aContext.setAvailableAsPerspectiveAction(false);
    }
}
