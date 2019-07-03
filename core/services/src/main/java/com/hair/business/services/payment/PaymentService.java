package com.hair.business.services.payment;

import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Address;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.tax.ComputeTaxResponse;
import com.hair.business.services.payment.braintree.BraintreePaymentService;

import java.util.List;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * StyleRequest Payment processor
 */
public interface PaymentService extends BraintreePaymentService {

    /**
     * Deduct one time payment with AddOns
     * */
    void deductNonPreAuthPayment(String paymentToken, List<AddOn> addOns);

    /**
     * Fires an refund request to downstream
     *
     * Useful if the payment is part of an existing style request
     * */
    void refund(Long styleRequestId, double amount);

    /**
     *
     * Computes how much tax is deductible based on country
     * */
    ComputeTaxResponse computeTax(String stylerequestID, String styleName, double servicePrice, Address merchantAddress, Address customerAddress, List<AddOn> addOns);

    void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault);
}
