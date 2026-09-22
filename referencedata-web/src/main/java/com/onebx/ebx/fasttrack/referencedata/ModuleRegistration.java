package com.onebx.ebx.fasttrack.referencedata;

import com.orchestranetworks.module.ModuleRegistrationListener;

import jakarta.servlet.annotation.WebListener;

@WebListener
public class ModuleRegistration extends ModuleRegistrationListener {
    @Override
    public void handleServiceRegistration(com.orchestranetworks.module.ModuleServiceRegistrationContext context) {
        context.registerUserService(new ReferenceDataHubUserServiceDeclaration());
        context.registerUserService(new FetchNuccDataUserServiceDeclaration());
    }
}
