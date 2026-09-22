package com.onebx.ebx.fasttrack.referencedata;

import com.orchestranetworks.service.ServiceKey;
import com.orchestranetworks.ui.selection.DataspaceEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.schema.types.dataspace.DataspaceSet;
import com.orchestranetworks.userservice.declaration.ActivationContextOnDataspace;
import com.orchestranetworks.userservice.declaration.UserServiceDeclaration;
import com.orchestranetworks.userservice.declaration.UserServicePropertiesDefinitionContext;
import com.orchestranetworks.userservice.declaration.WebComponentDeclarationContext;

public class ReferenceDataHubUserServiceDeclaration implements UserServiceDeclaration.OnDataspace {

    public static final ServiceKey SERVICE_KEY = ServiceKey.forModuleServiceName(ModuleNames.REFERENCE_DATA, "ReferenceDataHub");

    @Override
    public ServiceKey getServiceKey() {
        return SERVICE_KEY;
    }

    @Override
    public UserService<DataspaceEntitySelection> createUserService() {
        return new ReferenceDataHubUserService();
    }

    @Override
    public void defineActivation(ActivationContextOnDataspace aContext) {
        aContext.includeAllDataspaces(DataspaceSet.DataspaceType.ALL);
    }

    @Override
    public void defineProperties(UserServicePropertiesDefinitionContext aContext) {
        aContext.setLabel("Reference Data Hub (Next.js)");
        aContext.setDescription("High-fidelity Custom React interface for Reference Data");
    }

    @Override
    public void declareWebComponent(WebComponentDeclarationContext aContext) {
        // This makes the service available in the Perspective configuration dropdown!
        aContext.setAvailableAsPerspectiveAction(true);
    }
}
