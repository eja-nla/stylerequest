package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class Imposition extends AbstractBean {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
