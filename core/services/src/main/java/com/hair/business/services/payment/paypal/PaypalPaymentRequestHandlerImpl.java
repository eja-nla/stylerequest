package com.hair.business.services.payment.paypal;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.services.com.hair.business.client.AbstractClient;
import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Capture;
import com.paypal.api.payments.Payment;
import com.paypal.base.codec.binary.Base64;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

import org.apache.http.impl.client.CloseableHttpClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Paypal Payment Requests handler impl.
 * Fires actual calls to paypal
 *
 * Created by Olukorede Aguda on 26/02/2017.
 */
public class PaypalPaymentRequestHandlerImpl extends AbstractClient<Object> implements PaymentRequestHandler {

    private static final Logger logger = getLogger(PaypalPaymentRequestHandlerImpl.class);

    private static final String paypal_api_base = "https://api.sandbox.paypal.com";
    private static final String ACCESS_TOKEN_ENDPOINT = paypal_api_base + "/v1/oauth2/token";
    private static final String PAYMENT_ENDPOINT = paypal_api_base + "/v1/payments/payment";
    private static final String AUTHORIZATION_ENDPOINT = paypal_api_base + "/v1/payments/authorization/%s";
    private static final String CAPTURE_REQUEST_ENDPOINT = paypal_api_base + "/v1/payments/authorization/%s/capture";

    private AccessTokenResponse accessTokenResponse;
    private DateTime tokenExpiry;

    @Inject
    public PaypalPaymentRequestHandlerImpl(ObjectMapper objectMapper, Provider<CloseableHttpClient> clientProvider) {
        this(clientProvider.get(), objectMapper);
    }

    public PaypalPaymentRequestHandlerImpl(CloseableHttpClient client, ObjectMapper objectMapper) {
        super(client, objectMapper);
    }

    @Override
    public Payment issuePaymentRequest(Payment payment, APIContext paypalApiConftext) {
        try {

            final Map<String, String> headers = new HashMap<>(2);
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + fetchAccessToken().getAccess_token());

            return go(PAYMENT_ENDPOINT, headers, payment, Payment.class);
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | PayPalRESTException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public Authorization fetchAuthorization(APIContext paypalApiContext, String authorizationId) throws PayPalRESTException {
        try {
            final Map<String, String> headers = new HashMap<>(2);
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + fetchAccessToken().getAccess_token());

            return go(String.format(AUTHORIZATION_ENDPOINT, authorizationId), headers, null, Authorization.class);
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | PayPalRESTException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public Capture issueCaptureRequest(String authorizationId, APIContext paypalApiContext, Capture capture) throws PayPalRESTException {
        try {
            final Map<String, String> headers = new HashMap<>(2);
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + fetchAccessToken().getAccess_token());

            return go(String.format(CAPTURE_REQUEST_ENDPOINT, authorizationId), headers, capture, Capture.class);
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | PayPalRESTException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private <V> V go(String urlString, Map<String, String> headers, PayPalResource paypalPayload, Class<V> clazz) throws IOException, KeyManagementException, NoSuchAlgorithmException, PayPalRESTException {
        return doPost(urlString, headers, paypalPayload, clazz);
    }

    @Override
    public AccessTokenResponse fetchAccessToken() throws IOException {
        DateTime snapTime = DateTime.now();
        if (accessTokenResponse != null && !isExpired(snapTime)){
            if (accessTokenResponse.getAccess_token() != null) return accessTokenResponse;
        }

        String id_secret = System.getProperty("paypal.client.id") + ":" + System.getProperty("paypal.client.secret");

        byte[] encoded = Base64.encodeBase64(id_secret.getBytes("UTF-8"));
        String base64ClientID = new String(encoded);

        final Map<String, String> headers = new HashMap<>(3);
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept-Language", "en_US");
        headers.put("Authorization", "Basic " + base64ClientID);

        accessTokenResponse = doPost(ACCESS_TOKEN_ENDPOINT, headers, "grant_type=client_credentials", AccessTokenResponse.class);

        tokenExpiry = snapTime.plusSeconds(accessTokenResponse.getExpires_in() - 10); // some headroom

        return accessTokenResponse;

    }

    private boolean isExpired(DateTime snapTime) {
        return snapTime.isAfter(tokenExpiry);
    }

}
