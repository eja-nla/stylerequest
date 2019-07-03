package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class Tax extends AbstractBean {
    private String situsLocation;
    private Imposition jurisdiction; // for jurisdiction and imposition, we just need an entity that has a String 'value' field
    private Imposition imposition;
    private double taxableAmount;
    private double taxRate;
    private double taxAmount;

    public String getSitusLocation() {
        return situsLocation;
    }

    public void setSitusLocation(String situsLocation) {
        this.situsLocation = situsLocation;
    }

    public Imposition getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(Imposition jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public Imposition getImposition() {
        return imposition;
    }

    public void setImposition(Imposition imposition) {
        this.imposition = imposition;
    }

    public double getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(double taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }
}
