package com.hair.business.services.payment.stripe;

import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.TransactionResult;

import java.util.List;

/**
 * Stripe payment service
 *
 * Created by olukoredeaguda on 06/07/2020.
 */
public interface StripePaymentService {

    double COMMISSION = .15;
    double NO_SHOW_CHARGE = .20;

    TransactionResult createPaymentIntent(int amount, String customeStripeId, String refId);

    String createCustomer(String internalCustomerId);

    String createMerchant(String authCode, String stripeId);

    TransactionResult authorize(StyleRequest styleRequest, String chargeDescription); //will not do DB lookup
    TransactionResult authorize(Long styleRequestId, String chargeDescription);

    TransactionResult refund(StyleRequest styleRequest, int amount, List<AddOn> addOns);
    TransactionResult refund(Long styleRequestId, int amount, List<AddOn> addOns);

    TransactionResult cancel(Long styleRequestId);
    TransactionResult cancel(StyleRequest styleRequest);

    TransactionResult capture(StyleRequest styleRequest);
    TransactionResult capture(StyleRequest styleRequest, int price);
    TransactionResult capture(Long styleRequestId);

    TransactionResult chargeNow(StyleRequest styleRequest, List<AddOn> addOns);
    TransactionResult chargeNow(Long styleRequestId, List<AddOn> addOns);

    int calculateOrderAmount(int basePrice, List<AddOn> addOns);
}
