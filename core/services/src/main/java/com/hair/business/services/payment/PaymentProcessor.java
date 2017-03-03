package com.hair.business.services.payment;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * StyleRequestPayment processor
 */
public interface PaymentProcessor {

    /**
     * Fires an authorization request to downstream
     * */
    StyleRequestPayment holdPayment(StyleRequest styleRequest, Customer customer, double tax, double total);

    /**
     * Actually take the pre-authorized payment
     * */
    StyleRequestPayment deductPayment(String paymentIdIssuedAfterItsHeld, double totalAmount, boolean isFinalCapture);

    /**
     *
     * Computes how much tax is deductible based on country
     * */
    double computeTax(String countryCode, double itemPrice);

}
