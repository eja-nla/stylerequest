package com.hair.business.services.payment.paypal;

import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Capture;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Paypal Payment Requests handler impl.
 * Fires actual calls to paypal
 *
 * Created by Olukorede Aguda on 26/02/2017.
 */
public class PaypalPaymentRequestHandlerImpl implements PaymentRequestHandler {

    @Override
    public Payment issuePaymentRequest(Payment payment, APIContext paypalApiContext) throws PayPalRESTException {
        return payment.create(paypalApiContext);
    }

    @Override
    public Authorization issueAuthorizationRequest(APIContext paypalApiContext, String authorizationId) throws PayPalRESTException {
        return Authorization.get(paypalApiContext, authorizationId);
    }

    @Override
    public Capture issueCaptureRequest(Authorization authorization, APIContext paypalApiContext, Capture capture) throws PayPalRESTException {
        return authorization.capture(paypalApiContext, capture);
    }
}
