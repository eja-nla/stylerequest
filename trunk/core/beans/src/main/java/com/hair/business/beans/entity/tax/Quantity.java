package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class Quantity extends AbstractBean {
    private String unitOfMeasure;
    private double value;

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
