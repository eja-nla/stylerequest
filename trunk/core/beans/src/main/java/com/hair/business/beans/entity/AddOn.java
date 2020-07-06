package com.hair.business.beans.entity;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 *
 * Created by olukoredeaguda on 13/03/2017.
 */
public class AddOn extends AbstractBean {
    private String itemName;
    private String description;
    private int amount;
    private int totalAmount;
    private int quantity;
    private int tax;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalAmount() {
        return amount * quantity;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }
}
