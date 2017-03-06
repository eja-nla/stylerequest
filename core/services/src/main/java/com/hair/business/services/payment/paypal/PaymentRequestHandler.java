package com.hair.business.services.payment.paypal;

import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Capture;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.io.IOException;

/**
 * Paypal request handler
 *
 * Created by Olukorede Aguda on 26/02/2017.
 */

public interface PaymentRequestHandler {

    Payment issuePaymentRequest(Payment payment, APIContext paypalApiContext) throws PayPalRESTException;

    Authorization fetchAuthorization(APIContext paypalApiContext, String authorizationId) throws PayPalRESTException;

    AccessTokenResponse fetchAccessToken() throws IOException;

    Capture issueCaptureRequest(String id, APIContext paypalApiContext, Capture capture) throws PayPalRESTException;
}
