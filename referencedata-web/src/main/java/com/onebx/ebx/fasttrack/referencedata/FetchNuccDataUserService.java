package com.onebx.ebx.fasttrack.referencedata;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.UserServiceEventOutcome;
import com.orchestranetworks.userservice.UserServiceObjectContextBuilder;
import com.orchestranetworks.userservice.UserServicePane;
import com.orchestranetworks.userservice.UserServicePaneContext;
import com.orchestranetworks.userservice.UserServicePaneWriter;
import com.orchestranetworks.userservice.UserServiceProcessEventOutcomeContext;
import com.orchestranetworks.userservice.UserServiceSetupDisplayContext;
import com.orchestranetworks.userservice.UserServiceSetupObjectContext;
import com.orchestranetworks.userservice.UserServiceValidateContext;
import com.orchestranetworks.userservice.UserServiceDisplayConfigurator;
import com.onwbp.adaptation.Adaptation;

public class FetchNuccDataUserService implements UserService<TableViewEntitySelection> {

    @Override
    public void setupObjectContext(UserServiceSetupObjectContext<TableViewEntitySelection> aContext, UserServiceObjectContextBuilder aBuilder) {
        // No additional object context needed.
    }

    @Override
    public void setupDisplay(UserServiceSetupDisplayContext<TableViewEntitySelection> aContext, UserServiceDisplayConfigurator aConfigurator) {
        aConfigurator.setBottomBarDisplayed(true);
        final Adaptation dataset = aContext.getEntitySelection().getDataset();

        aConfigurator.setContent(new UserServicePane() {
            @Override
            public void writePane(UserServicePaneContext aPaneContext, UserServicePaneWriter aWriter) {
                String endpointUrl = dataset.getString(Path.parse("/root/Configuration/sourceEndpointUrl"));

                aWriter.add("<div style=\"margin: 20px;\">");
                aWriter.add("<h3>Fetch Latest NUCC Data</h3>");

                if (endpointUrl == null || endpointUrl.trim().isEmpty()) {
                    aWriter.add("<p style=\"color: red;\"><strong>Error:</strong> The Source Endpoint URL is not configured. Please configure it in the Configuration group at the root level.</p>");
                } else {
                    aWriter.add("<p>Configured Endpoint: <strong>").addSafeInnerHTML(endpointUrl).add("</strong></p>");
                    aWriter.add("<p>Executing this service will reach out to the specified endpoint, download the CSV, and merge the updates directly into this table.</p>");
                    
                    aWriter.add("<br/>");
                    aWriter.add("<div style=\"padding: 15px; border: 1px solid #ccc; background: #f9f9f9;\">");
                    aWriter.add("<p><em>(Fetch procedure execution logic to be implemented here)</em></p>");
                    aWriter.add("</div>");
                }

                aWriter.add("</div>");
            }
        });
    }

    @Override
    public void validate(UserServiceValidateContext<TableViewEntitySelection> aContext) {
    }

    @Override
    public UserServiceEventOutcome processEventOutcome(UserServiceProcessEventOutcomeContext<TableViewEntitySelection> aContext, UserServiceEventOutcome anEventOutcome) {
        return anEventOutcome;
    }
}
