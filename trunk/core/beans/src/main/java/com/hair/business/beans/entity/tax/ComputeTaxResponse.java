package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class ComputeTaxResponse extends AbstractBean {


    private TaxResponse computeTaxResponse;
    private boolean hasError;

    public ComputeTaxResponse() {
    }

    public ComputeTaxResponse(TaxResponse computeTaxResponse) {
        this.computeTaxResponse = computeTaxResponse;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public TaxResponse getComputeTaxResponse() {
        return computeTaxResponse;
    }

    public void setComputeTaxResponse(TaxResponse computeTaxResponse) {
        this.computeTaxResponse = computeTaxResponse;
    }
}
