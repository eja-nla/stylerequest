package com.hair.business.services.payment.stripe;

import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.TransactionResult;

import java.util.List;

/**
 * Braintree payment service
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public interface StripePaymentService {

    TransactionResult createPaymentIntent(int amount, String customeStripeId);

    String createCustomer(String internalCustomerId);

    String createMerchant(String authCode, String stripeId);

    TransactionResult authorize(StyleRequest styleRequest, String chargeDescription); //will not do DB lookup
    TransactionResult authorize(Long styleRequestId, String chargeDescription);

    TransactionResult refund(StyleRequest styleRequest, List<AddOn> addOns);
    TransactionResult refund(Long styleRequestId, List<AddOn> addOns);

    TransactionResult cancelPayment(Long styleRequestId);
    TransactionResult cancelPayment(StyleRequest styleRequest);

    TransactionResult capture(StyleRequest styleRequest);
    TransactionResult capture(Long styleRequestId);

    TransactionResult chargeNow(StyleRequest styleRequest, List<AddOn> addOns);
    TransactionResult chargeNow(Long styleRequestId, List<AddOn> addOns);

    int calculateOrderAmount(int basePrice, List<AddOn> addOns);
}
