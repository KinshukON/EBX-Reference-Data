package com.onebx.ebx.fasttrack.referencedata;

import com.orchestranetworks.module.ModuleRegistrationListener;

import jakarta.servlet.annotation.WebListener;

@WebListener
public final class ModuleRegistration extends ModuleRegistrationListener {
    @Override
    public void handleServiceRegistration(final com.orchestranetworks.module.ModuleServiceRegistrationContext context) {
        super.handleServiceRegistration(context);
        context.registerUserService(new ReferenceDataHubUserServiceDeclaration());
        context.registerUserService(new FetchNuccDataUserServiceDeclaration());
    }
}
