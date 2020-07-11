package com.x.business.notif;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.StyleRequest;

import org.apache.commons.lang3.tuple.Pair;

/**
 * AbstractEmailNotification object
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class CancelledStyleRequestNotification extends AbstractStyleRequestNotificationTask {

    private String tokenizedCustomerEmailBody;
    private String tokenizedMerchantEmailBody;
    private Preferences merchantPreferences;

    private static final Pair<String, String> merchant_customer_template_pair = getTemplatePair(
            System.getProperty("sendgrid.cancelled.customer.stylerequest.email.template"),
            System.getProperty("sendgrid.cancelled.merchant.stylerequest.email.template")
    );

    public CancelledStyleRequestNotification(StyleRequest styleRequest) {

        this.merchantPreferences = styleRequest.getMerchant().getPreferences();
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
