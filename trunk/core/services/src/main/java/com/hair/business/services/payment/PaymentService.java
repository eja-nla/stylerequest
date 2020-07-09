//package com.hair.business.services.payment;
//
//import com.hair.business.beans.constants.PaymentType;
//import com.hair.business.beans.entity.AddOn;
//import com.hair.business.beans.entity.Address;
//import com.hair.business.beans.entity.PaymentMethod;
//import com.hair.business.beans.entity.tax.ComputeTaxResponse;
//import com.hair.business.services.payment.braintree.BraintreePaymentService;
//
//import java.util.List;
//
///**
// * Created by olukoredeaguda on 09/02/2017.
// *
// *  Payment workflow looks like this:
// *     //1. 7 days or less before appointment, charge the known total to the card (could be the estimatedPrice with or without addons)
// *              We need 7 days or less because payment auth and charge only works within 7 days between this auth and the corresponding charge in step 3
// *     //2. if customer/merchant cancel before the day, release the charge. No payment needed
// *     //3. if they cancel on the day or don't show up, capture 20% of charge from 1
// *     //4. when appt is completed, cancel the charge from 1, then authorize and charge (chargeNow) the total (style+addOn)
// *
// * StyleRequest Payment processor
// */
//public interface PaymentService extends BraintreePaymentService {
//
//
//
//    String createCustomerProfile(String internalCustomerId);
//    String createMerchantProfile(String authorizationCode, String state);
//
//    /**
//     * Deduct one time payment with AddOns
//     * */
//    void deductNonPreAuthPayment(String transactionId, List<AddOn> addOns);
//
//    /**
//     * Fires an refund request to downstream
//     *
//     * Useful if the payment is part of an existing style request
//     * */
//    void refund(Long styleRequestId, double amount);
//
//    /**
//     *
//     * Computes how much tax is deductible based on country
//     * */
//    ComputeTaxResponse computeTax(String stylerequestID, String styleName, double servicePrice, Address merchantAddress, Address customerAddress, List<AddOn> addOns);
//
//    void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault);
//
//}
