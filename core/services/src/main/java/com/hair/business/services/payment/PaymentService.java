package com.hair.business.services.payment;

import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * StyleRequest Payment processor
 */
public interface PaymentService {

    /**
     * Fires an authorization request to downstream
     * */
    StyleRequestPayment holdPayment(StyleRequest styleRequest, Customer customer);

    /**
     * Actually take the pre-authorized payment
     * */
    StyleRequestPayment deductPayment(String authorizationId, double totalAmount, boolean isFinalCapture);

    /**
     * Fires an refund request to downstream
     * */
    StyleRequestPayment refund(StyleRequest styleRequest, Customer customer);

    /**
     *
     * Computes how much tax is deductible based on country
     * */
    double computeTax(String countryCode, double itemPrice);

    void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault);
}
