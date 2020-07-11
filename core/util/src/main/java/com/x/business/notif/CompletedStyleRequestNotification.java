package com.x.business.notif;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.StyleRequest;

import org.apache.commons.lang3.tuple.Pair;

/**
 * AbstractEmailNotification object
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class CompletedStyleRequestNotification extends AbstractStyleRequestNotificationTask {

    private String tokenizedCustomerEmailBody;
    private String tokenizedMerchantEmailBody;
    private Preferences merchantPreferences;

    private static final Pair<String, String> merchant_customer_template_pair = getTemplatePair(
            System.getProperty("sendgrid.completed.customer.stylerequest.email.template"),
            System.getProperty("sendgrid.completed.merchant.stylerequest.email.template")
    );

    public CompletedStyleRequestNotification(StyleRequest styleRequest) {

        this.merchantPreferences = styleRequest.getMerchant().getPreferences();
        this.tokenizedCustomerEmailBody = tokenizeCustomer(styleRequest);
        this.tokenizedMerchantEmailBody = merchantPreferences.isCompletedNotificationEnabled() ? tokenizeMerchant(styleRequest) : null;
    }


    @Override
    public String getCustomerEmailBody() {
        return tokenizedCustomerEmailBody;
    }

    @Override
    public String getMerchantEmailBody() {
        return tokenizedMerchantEmailBody;
    }

    @Override
    protected boolean shouldSendToMerchant() {
        return merchantPreferences.isCompletedNotificationEnabled();
    }

    private String tokenizeCustomer(StyleRequest styleRequest){
        return String.format(merchant_customer_template_pair.getLeft(),
                styleRequest.getMerchant().getEmail(),
                styleRequest.getCustomer().getFirstName(),
                styleRequest.getStyle().getName(),
                styleRequest.getAppointmentStartTime().toDate(),
                styleRequest.getMerchant().getBusinessName(),
                getFromEmail()
        );
    }

    private String tokenizeMerchant(StyleRequest styleRequest){
        return String.format(merchant_customer_template_pair.getRight(),
                styleRequest.getMerchant().getEmail(),
                styleRequest.getCustomer().getFirstName(),
                styleRequest.getStyle().getName(),
                styleRequest.getAppointmentStartTime().toDate(),
                styleRequest.getMerchant().getBusinessName(),
                getFromEmail()
        );
    }
}
