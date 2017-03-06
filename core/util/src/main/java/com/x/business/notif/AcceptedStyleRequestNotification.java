package com.x.business.notif;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.StyleRequest;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Accepted Style Request Notification
 *
 * Creates notification contents available for sending based on recipient's preferences
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class AcceptedStyleRequestNotification extends AbstractStyleRequestNotificationTask {

    private final String tokenizedCustomerEmailBody;
    private final String tokenizedMerchantEmailBody;
    private Preferences merchantPreferences;

    private static final Pair<String, String> merchant_customer_template_pair = getTemplatePair(
            System.getProperty("sendgrid.accepted.customer.stylerequest.email.template"),
            System.getProperty("sendgrid.accepted.merchant.stylerequest.email.template")
    );

    public AcceptedStyleRequestNotification(StyleRequest styleRequest, Preferences preferences) {

        this.merchantPreferences = preferences;
        this.tokenizedCustomerEmailBody = tokenizeCustomer(styleRequest);
        this.tokenizedMerchantEmailBody = merchantPreferences.isAcceptedNotificationEnabled() ? tokenizeMerchant(styleRequest) : null;
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
        return merchantPreferences.isAcceptedNotificationEnabled();
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
