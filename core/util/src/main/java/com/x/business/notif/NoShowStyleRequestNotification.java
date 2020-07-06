package com.x.business.notif;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.StyleRequest;

import org.apache.commons.lang3.tuple.Pair;

/**
 * NoShowStyleRequestNotification object
 * Created by Olukorede Aguda on 06/07/2020.
 *
 * Notify user when Stylist clicks "No show" for an appointment. Encourage stylists to only do this at the end of the day (give clients grace)
 */
public class NoShowStyleRequestNotification extends AbstractStyleRequestNotificationTask {

    private String tokenizedCustomerEmailBody;
    private String tokenizedMerchantEmailBody;
    private Preferences merchantPreferences;

    private static final Pair<String, String> merchant_customer_template_pair = getTemplatePair(
            System.getProperty("sendgrid.noshow.customer.stylerequest.email.template"),
            System.getProperty("sendgrid.noshow.merchant.stylerequest.email.template")
    );

    public NoShowStyleRequestNotification(StyleRequest styleRequest, Preferences preferences) {

        this.merchantPreferences = preferences;
        this.tokenizedCustomerEmailBody = tokenizeCustomer(styleRequest);
        this.tokenizedMerchantEmailBody = merchantPreferences.isCancelledNotificationEnabled() ? tokenizeMerchant(styleRequest) : null;
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
        return merchantPreferences.isCancelledNotificationEnabled();
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
