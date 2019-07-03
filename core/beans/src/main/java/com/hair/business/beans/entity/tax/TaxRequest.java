package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

import java.util.List;

/**
 * Created by ejanla on 7/1/19.
 */
public class TaxRequest extends AbstractBean {

    public TaxRequest() {
    }

    public TaxRequest(String transactionDate, String transactionType) {
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }

    private String transactionDate;
    private String transactionType;
    private Currency currency;
    private Seller seller;
    private Buyer buyer;
    private List<LineItem> lineItems;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
