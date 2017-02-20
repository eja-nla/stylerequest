package com.x.business.notif;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.StyleRequest;
import com.x.business.utilities.Assert;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Placed Style Request Notification
 *
 * Creates notification contents available for sending based on recipient's preferences
 *
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class PlacedStyleRequestNotification extends AbstractStyleRequestNotification {

    private String tokenizedCustomerEmailBody;
    private String tokenizedMerchantEmailBody;
    private Preferences merchantPreferences;

    private static final Pair<String, String> merchant_customer_template_pair = getTemplatePair(
            System.getProperty("sendgrid.placed.customer.stylerequest.email.template"),
            System.getProperty("sendgrid.placed.merchant.stylerequest.email.template")
    );

    public PlacedStyleRequestNotification(StyleRequest styleRequest, Preferences merchantPreferences) {

        Assert.notNull(merchantPreferences, "Merchant email Preference is required.");
        this.merchantPreferences = merchantPreferences;
        this.tokenizedCustomerEmailBody = tokenizeCustomer(styleRequest);
        this.tokenizedMerchantEmailBody = merchantPreferences.isPlacedNotificationEnabled() ? tokenizeMerchant(styleRequest) : null;
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
        return merchantPreferences.isPlacedNotificationEnabled();
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
