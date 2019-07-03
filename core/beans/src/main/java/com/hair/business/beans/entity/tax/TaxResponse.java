package com.hair.business.beans.entity.tax;

import java.util.List;

/**
 * Created by ejanla on 6/30/19.
 */
public class TaxResponse {
    private double total;
    private double subTotal;
    private double totalTax;
    private List<LineItem> lineItems;
    private String taxResultType;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

}
