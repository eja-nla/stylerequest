package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class ComputeTaxRequest extends AbstractBean {

    private TaxRequest computeTaxRequest;

    public ComputeTaxRequest() {
    }

    public ComputeTaxRequest(TaxRequest computeTaxRequest) {
        this.computeTaxRequest = computeTaxRequest;
    }

    public TaxRequest getComputeTaxRequest() {
        return computeTaxRequest;
    }

    public void setComputeTaxRequest(TaxRequest computeTaxRequest) {
        this.computeTaxRequest = computeTaxRequest;
    }
}
