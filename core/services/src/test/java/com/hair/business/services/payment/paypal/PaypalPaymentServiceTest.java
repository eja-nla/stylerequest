package com.hair.business.services.payment.paypal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.customer.AbstractServicesTestBase;
import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Capture;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 * Created by Olukorede Aguda on 25/02/2017.
 */
public class PaypalPaymentServiceTest extends AbstractServicesTestBase {

    private Repository repository;
    private final APIContext paypalApiContext = mock(APIContext.class);
    private PaypalPaymentService paypalPaymentProcessor;
    private final PaymentRequestHandler paypalPaymentRequestHandler = mock(PaymentRequestHandler.class);

    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);

        try {
            when(paypalPaymentRequestHandler.fetchAuthorization(any(APIContext.class), anyString())).thenReturn(new Authorization());
            when(paypalPaymentRequestHandler.issueCaptureRequest(anyString(), any(APIContext.class), any(Capture.class))).thenReturn(new Capture());
            when(paypalPaymentRequestHandler.issuePaymentRequest(any(Payment.class), any(APIContext.class))).thenReturn(new Payment());
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        paypalPaymentProcessor = new PaypalPaymentServiceImpl(paypalApiContext, repository, paypalPaymentRequestHandler);
    }

    @Test
    public void testauthorizePayment() throws Exception {
    //    StyleRequestPayment styleRequestPayment = paypalPaymentProcessor.authorizePayment(createStyleRequest(), createProfile(), 34.54, 676.34);


    }

    @Test
    public void testCapturePreauthorizedPayment() throws Exception {

    }

}