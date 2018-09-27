package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Serialize;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

import java.io.Serializable;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * Payment method really can be anything from paypal account to credit card or whatever
 *
 * Ideally, we would just want to save whatever got returned from the gateway
 * but objectify/datastore restrictions are forcing us to have to copy the response into an
 * entity that can be registered.
 *
 * Since there could be a myriad combination of payment methods,
 * we use this class to store useful bits in whatever got returned.
 *
 * Note: Storing Creditcard info requires PCI compliance, and we don't want to get involved in that business
 * So what we do is
 * 1. Client submits payment method
 * 2. returns us a nonce
 * 3. We use returned nonce to create customer in braintree's vault
 */
@Serialize
public class PaymentMethod extends AbstractActorEnablerEntity implements Serializable{

    public PaymentMethod() {}

    // For credit cards
    private String type;

    public PaymentMethod(String billingAgreementId, boolean isDefault, String customerId) {
        this.billingAgreementId = billingAgreementId;
        this.isDefault = isDefault;
        this.customerId = customerId;
    }

    private String billingAgreementId; // what is this?
    private boolean isDefault;
    private String imageUrl;
    private String customerId;

    private String token;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getBillingAgreementId() {
        return billingAgreementId;
    }

    public void setBillingAgreementId(String billingAgreementId) {
        this.billingAgreementId = billingAgreementId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
