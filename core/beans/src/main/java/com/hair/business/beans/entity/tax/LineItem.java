package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

import java.util.List;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class LineItem extends AbstractBean {
    private String lineItemId;
    private Product product;
    private Quantity quantity;
    private String productName;
    private UnitPrice unitPrice;
    private double subTotalAmount;
    private double subTotalTax;
    private List<Tax> taxes;

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public UnitPrice getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(UnitPrice unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(double subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }

    public double getSubTotalTax() {
        return subTotalTax;
    }

    public void setSubTotalTax(double subTotalTax) {
        this.subTotalTax = subTotalTax;
    }

    public List<Tax> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Tax> taxes) {
        this.taxes = taxes;
    }
}
