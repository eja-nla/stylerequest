package com.hair.business.services.tax;

import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Address;
import com.hair.business.beans.entity.tax.ComputeTaxResponse;

import java.util.List;

/**
 * Created by ejanla on 7/8/20.
 */
public interface SalesTaxService {
    ComputeTaxResponse computeTax(String stylerequestID, String styleName, int servicePrice, Address merchantAddress, Address customerAddress, List<AddOn> addOns);
}
