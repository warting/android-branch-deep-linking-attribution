package io.branch.referral.validators;

import static io.branch.referral.validators.IntegrationValidatorConstants.alternateDomainsMoreInfoDocsLink;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONObject;

public class AlternateDomainsCheck extends IntegrationValidatorCheck {
    String name = "Alt Domains";
    String errorMessage = "Could not find intent filter to support alternate link domain. Please add intent filter for handling alternate link domain in your Android Manifest file";
    String moreInfoLink = alternateDomainsMoreInfoDocsLink;
    BranchIntegrationModel integrationModel;
    JSONObject branchAppConfig;

    public AlternateDomainsCheck(BranchIntegrationModel integrationModel, JSONObject branchAppConfig) {
        super.name = name;
        super.errorMessage = errorMessage;
        super.moreInfoLink = moreInfoLink;
        this.integrationModel = integrationModel;
        this.branchAppConfig = branchAppConfig;
    }

    @Override
    public boolean RunTests(Context context) {
        String alternateAppLinkDomain = branchAppConfig.optString("alternate_short_url_domain");
        return TextUtils.isEmpty(alternateAppLinkDomain) || checkIfIntentAddedForLinkDomain(alternateAppLinkDomain);
    }

    @Override
    public String GetOutput(Context context, boolean didTestSucceed) {
        didTestSucceed = RunTests(context);
        return super.GetOutput(context, didTestSucceed);
    }

    private boolean checkIfIntentAddedForLinkDomain(String domainName) {
        boolean foundIntentFilterMatchingDomainName = false;
        if (!TextUtils.isEmpty(domainName)) {
            for (String host : integrationModel.applinkScheme) {
                if (domainName.equals(host)) {
                    foundIntentFilterMatchingDomainName = true;
                    break;
                }
            }
        }
        return foundIntentFilterMatchingDomainName;
    }
}
