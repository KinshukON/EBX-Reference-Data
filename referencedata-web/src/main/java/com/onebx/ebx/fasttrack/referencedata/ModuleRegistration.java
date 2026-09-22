package com.onebx.ebx.fasttrack.referencedata;

import com.orchestranetworks.module.ModuleRegistrationListener;
import com.orchestranetworks.module.ModuleContextOnRepositoryStartup;
import com.orchestranetworks.service.OperationException;

import jakarta.servlet.annotation.WebListener;

@WebListener
public final class ModuleRegistration extends ModuleRegistrationListener {
    @Override
    public void handleRepositoryStartup(final ModuleContextOnRepositoryStartup context) throws OperationException {
        super.handleRepositoryStartup(context);
        ReferenceDataBootstrap.install(
            context.getRepository(),
            context.createSystemUserSession("reference-data-bootstrap"),
            context.getLoggingCategory());
    }

    @Override
    public void handleServiceRegistration(final com.orchestranetworks.module.ModuleServiceRegistrationContext context) {
        super.handleServiceRegistration(context);
        context.registerUserService(new ReferenceDataHubUserServiceDeclaration());
        context.registerUserService(new FetchNuccDataUserServiceDeclaration());
    }
}
