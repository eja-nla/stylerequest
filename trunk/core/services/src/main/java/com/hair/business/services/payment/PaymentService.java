package com.hair.business.services.payment;

import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;

import java.util.List;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * StyleRequest Payment processor
 */
public interface PaymentService {

    /**
     * Fires an authorization request to downstream.
     *
     * Some implementations of this require that the passed in customer's
     * information exist in the provider's vault e.g. Braintree.
     * */
    StyleRequest holdPayment(StyleRequest styleRequest, Customer customer);

    /**
     * Actually take the pre-authorized payment
     * */
    StyleRequest deductPreAuthPayment(Long styleRequestId, double totalAmount);

    /**
     * Deduct one time payment with AddOns
     * */
    void deductNonPreAuthPayment(String paymentToken, List<AddOn> addOns);

    Customer createCustomerPaymentProfile(Customer customer, PaymentType paymentType, boolean isDefault);

    /**
     * Fires an refund request to downstream
     * */
    void refund(StyleRequest styleRequest);

    /**
     *
     * Computes how much tax is deductible based on country
     * */
    double computeTax(String countryCode, double itemPrice);

    void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault);
}
