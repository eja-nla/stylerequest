package com.hair.business.services.payment;

import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;

import java.util.List;

/**
 * No-op payment service. Does nothing
 *
 * Created by olukoredeaguda on 31/03/2017.
 */
public class NoopPaymentServiceImpl implements PaymentService {
    @Override
    public StyleRequest holdPayment(StyleRequest styleRequest, Customer customer) {
        return null;
    }

    @Override
    public StyleRequest deductPreAuthPayment(Long styleRequestId, double totalAmount) {
        return null;
    }

    @Override
    public void deductNonPreAuthPayment(String paymentToken, List<AddOn> addOns) {
    }

    @Override
    public Customer createCustomerPaymentProfile(Customer customer, PaymentType paymentType, boolean isDefault) {
        return null;
    }

    @Override
    public void refund(StyleRequest styleRequest) {
    }

    @Override
    public double computeTax(String countryCode, double itemPrice) {
        return 0;
    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault) {
    }
}
