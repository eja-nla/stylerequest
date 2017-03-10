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
 * Since there could be a myrad combination of payment methods,
 * we use this class to store useful bits in whatever got returned.
 */
@Serialize
public class PaymentMethod extends AbstractActorEnablerEntity implements Serializable{

    public PaymentMethod() {}

    // For credit cards
    private String type;
    private Long number;
    private int expireMonth;
    private int expireYear;
    private int cvv2;
    private String firstName;
    private String lastName;

    // For paypal account
    private String email;

    public PaymentMethod(String email, String billingAgreementId, boolean isDefault, String customerId) {
        this.email = email;
        this.billingAgreementId = billingAgreementId;
        this.isDefault = isDefault;
        this.customerId = customerId;
    }

    private String billingAgreementId;
    private boolean isDefault;
    private String imageUrl;
    private String customerId;

    private String token;

    // for credit cards
    public PaymentMethod(String type, Long number, int expireMonth, int expireYear, int cvv2, String firstName, String lastName) {
        this.type = type;
        this.number = number;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.cvv2 = cvv2;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //for paypal accounts


//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public int getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(int expireMonth) {
        this.expireMonth = expireMonth;
    }

    public int getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(int expireYear) {
        this.expireYear = expireYear;
    }

    public int getCvv2() {
        return cvv2;
    }

    public void setCvv2(int cvv2) {
        this.cvv2 = cvv2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
