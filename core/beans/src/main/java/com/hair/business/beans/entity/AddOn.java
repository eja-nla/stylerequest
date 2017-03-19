package com.hair.business.beans.entity;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 *
 * Created by olukoredeaguda on 13/03/2017.
 */
public class AddOn extends AbstractBean {
    private String itemName;
    private String description;
    private double amount;
    private int quantity;

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
