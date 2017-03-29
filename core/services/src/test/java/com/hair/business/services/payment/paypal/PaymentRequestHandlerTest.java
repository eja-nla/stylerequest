package com.hair.business.services.payment.paypal;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Payment;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Provider;

/**
 *
 * Created by olukoredeaguda on 03/03/2017.
 */
@Ignore ("for now until payment is fully ready")
public class PaymentRequestHandlerTest {


    private static final CloseableHttpClient client = mock(CloseableHttpClient.class);

    private final AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
    private final Payment payment = new Payment();

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final Provider clientProvider = mock(Provider.class);
    private PaymentRequestHandler paymentRequestHandler;
    private final CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    private final StatusLine statusLine = mock(StatusLine.class);
    private final InputStream is = mock(InputStream.class);


    @Before
    public void setUp(){
        when(clientProvider.get()).thenReturn(client);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(mock(HttpEntity.class));

        try {
            when(client.execute(any(HttpPost.class))).thenReturn(response);
            when(response.getEntity().getContent()).thenReturn(is);
            when(objectMapper.readValue(is, AccessTokenResponse.class)).thenReturn(accessTokenResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        paymentRequestHandler = new PaypalPaymentRequestHandlerImpl(objectMapper, clientProvider);
    }

    @Test
    public void testIssuePaymentRequestOK() throws Exception {
        when(objectMapper.readValue(is, Payment.class)).thenReturn(payment);
        assertThat(paymentRequestHandler.issuePaymentRequest(payment, null), is(notNullValue()));
    }

    @Test
    public void testIssuePaymentRequestNull() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(500);
        assertThat(paymentRequestHandler.issuePaymentRequest(payment, null), nullValue());
    }

    @Test
    public void issueAuthorizationRequest() throws Exception {
    }

    @Test
    public void issueCaptureRequest() throws Exception {
    }

    @Test
    public void fetchAccessToken() throws Exception {
    }

}