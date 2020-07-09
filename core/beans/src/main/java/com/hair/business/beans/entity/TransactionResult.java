package com.hair.business.beans.entity;

import com.hair.business.beans.helper.PaymentOperation;

/**
 * Created by ejanla on 7/6/20.
 *
 * Stores the result from the payment request to the payment gateway such as Stripe or Braintree or whatever for audit purposes
 */
public class TransactionResult {
    private PaymentOperation operation;
    private int totalAmount;
    private String statusMessage;
    private String ownId;

    public TransactionResult() {
    }

    public TransactionResult(String ownPaymentId, PaymentOperation operation, int totalAmount, String statusMessage) {
        this.ownId = ownPaymentId;
        this.operation = operation;
        this.totalAmount = totalAmount;
        this.statusMessage = statusMessage;
    }

    public PaymentOperation getOperation() {
        return operation;
    }

    public void setOperation(PaymentOperation operation) {
        this.operation = operation;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setOwnId(String ownId) {
        this.ownId = ownId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getOwnId() {
        return ownId;
    }

}
