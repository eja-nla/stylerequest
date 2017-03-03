package com.hair.business.services.payment.paypal;

import static java.util.logging.Logger.getLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Capture;
import com.paypal.api.payments.Payment;
import com.paypal.base.codec.binary.Base64;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.joda.time.DateTime;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Paypal Payment Requests handler impl.
 * Fires actual calls to paypal
 *
 * Created by Olukorede Aguda on 26/02/2017.
 */
public class PaypalPaymentRequestHandlerImpl implements PaymentRequestHandler {

    private static final Logger logger = getLogger(PaypalPaymentRequestHandlerImpl.class.getName());

    private static final String paypal_api_base = "https://api.sandbox.paypal.com";
    private static final String ACCESS_TOKEN_ENDPOINT = paypal_api_base + "/v1/oauth2/token";
    private static final String AUTHORIZE_PAYMENT_ENDPOINT = paypal_api_base + "/v1/payments/payment";

    private static final SSLConnectionSocketFactory sf = getSSLContext();
    private static final CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sf).build();

    private AccessTokenResponse accessTokenResponse;
    private ObjectMapper objectMapper;
    private DateTime tokenExpiry;

    @Inject
    public PaypalPaymentRequestHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Payment issuePaymentRequest(Payment payment, APIContext paypalApiContext) {
        Payment newPayment = null;
        try {
             newPayment = go(AUTHORIZE_PAYMENT_ENDPOINT, payment, Payment.class);
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException | PayPalRESTException e) {
            logger.severe(e.getMessage());
        }

        //return payment.create(paypalApiContext);
        return newPayment;
    }

    @Override
    public Authorization issueAuthorizationRequest(APIContext paypalApiContext, String authorizationId) throws PayPalRESTException {
        return Authorization.get(paypalApiContext, authorizationId);
    }

    @Override
    public Capture issueCaptureRequest(Authorization authorization, APIContext paypalApiContext, Capture capture) throws PayPalRESTException {
        return authorization.capture(paypalApiContext, capture);
    }

    private  <T extends PayPalResource> T go(String urlString, T paypalPayload, Class<T> clazz) throws IOException, KeyManagementException, NoSuchAlgorithmException, PayPalRESTException {

        HttpPost request = new HttpPost(urlString);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer " + fetchAccessToken().getAccess_token());
        request.setEntity(new StringEntity(paypalPayload.toJSON()));

        HttpResponse response = client.execute(request);
        logger.info("Paypal response " + response);

        if (response.getStatusLine().getStatusCode() == 200) {
            return objectMapper.readValue(response.getEntity().getContent(), clazz);
        }

        throw new PayPalRESTException("Paypal request unsuccessful with Response " + response);
    }

    @Override
    public AccessTokenResponse fetchAccessToken() throws IOException {
        DateTime snapTime = DateTime.now();
        if (accessTokenResponse != null && !isExpired(snapTime)){
            if (accessTokenResponse.getAccess_token() != null) return accessTokenResponse;
        }

        HttpPost request = new HttpPost(ACCESS_TOKEN_ENDPOINT);
        request.addHeader("Accept", "application/json");
        request.addHeader("Accept-Language", "en_US");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");

        String id_secret = System.getProperty("paypal.client.id") + ":" + System.getProperty("paypal.client.secret");

        byte[] encoded = Base64.encodeBase64(id_secret.getBytes("UTF-8"));
        String base64ClientID = new String(encoded);
        request.addHeader("Authorization", "Basic " + base64ClientID);

        request.setEntity(new StringEntity("grant_type=client_credentials"));

        HttpResponse response = client.execute(request);

        accessTokenResponse = objectMapper.readValue(response.getEntity().getContent(), AccessTokenResponse.class);

        tokenExpiry = snapTime.plusSeconds(accessTokenResponse.getExpires_in() - 10); // some headroom

        return accessTokenResponse;

    }

    private boolean isExpired(DateTime snapTime) {
        return snapTime.isAfter(tokenExpiry);
    }

    private static SSLConnectionSocketFactory getSSLContext(){
        try {
            return new SSLConnectionSocketFactory(SSLContextBuilder.create().useProtocol("TLSv1.2").build(),
                    new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
                    null, new DefaultHostnameVerifier());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }
}
