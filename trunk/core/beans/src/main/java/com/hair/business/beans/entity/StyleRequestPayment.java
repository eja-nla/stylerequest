package com.hair.business.beans.entity;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.tax.ComputeTaxResponse;
import com.hair.business.beans.helper.PaymentStatus;

/**
 * Represents a payment between two entities.
 *
 *  Created by Olukorede Aguda on 25/04/2016.
 *
 */
@Entity
public class StyleRequestPayment extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    private Double totalAmount;
    private Double price;
    private Long customerId;
    private Long merchantId;
    private String transactionId;

    private boolean settled;
    private PaymentType type;
    private ComputeTaxResponse taxDetails;
    private @Index PaymentStatus paymentStatus;

    public StyleRequestPayment(){}

    public StyleRequestPayment(Double totalAmount, Long customerId, Long merchantId, boolean settled, PaymentType type) {
        this();
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.settled = settled;
        this.type = type;
    }

    public StyleRequestPayment(double totalAmount, Long customerId, Long merchantId, boolean isSettled) {
        super();

        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.settled = isSettled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public ComputeTaxResponse getTaxDetails() {
        return taxDetails;
    }

    public void setTaxDetails(ComputeTaxResponse taxDetails) {
        this.taxDetails = taxDetails;
    }
}
