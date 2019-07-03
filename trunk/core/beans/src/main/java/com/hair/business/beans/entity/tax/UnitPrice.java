package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class UnitPrice extends AbstractBean {
    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
