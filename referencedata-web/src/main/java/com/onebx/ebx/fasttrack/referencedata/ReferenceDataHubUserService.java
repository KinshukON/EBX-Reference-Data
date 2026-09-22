package com.onebx.ebx.fasttrack.referencedata;

import com.orchestranetworks.ui.selection.DataspaceEntitySelection;
import com.orchestranetworks.userservice.*;

public class ReferenceDataHubUserService implements UserService<DataspaceEntitySelection> {

    @Override
    public void setupObjectContext(UserServiceSetupObjectContext<DataspaceEntitySelection> aContext, UserServiceObjectContextBuilder aBuilder) {
        // No object context required for an iframe wrapper
    }

    @Override
    public void setupDisplay(UserServiceSetupDisplayContext<DataspaceEntitySelection> aContext, UserServiceDisplayConfigurator aConfigurator) {
        // Remove standard EBX headers and padding to allow the Next.js app to take full screen
        aConfigurator.setHeaderDisplayed(false);
        aConfigurator.setBottomBarDisplayed(false);
        
        aConfigurator.setContent(new UserServiceRawPane() {
            @Override
            public void writePane(UserServiceRawPaneContext aPaneContext, UserServiceRawPaneWriter aWriter) {
                // Embed the Next.js application running on port 3001
                aWriter.add("<iframe src=\"http://localhost:3001/reference-data\" ");
                aWriter.add("style=\"width: 100%; height: 100vh; border: none; margin: 0; padding: 0; display: block;\"");
                aWriter.add("></iframe>");
            }
        });
    }

    @Override
    public void validate(UserServiceValidateContext<DataspaceEntitySelection> aContext) {
    }

    @Override
    public UserServiceEventOutcome processEventOutcome(UserServiceProcessEventOutcomeContext<DataspaceEntitySelection> aContext, UserServiceEventOutcome anEventOutcome) {
        return null;
    }
}
