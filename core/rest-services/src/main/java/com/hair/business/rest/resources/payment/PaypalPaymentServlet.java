package com.hair.business.rest.resources.payment;

import static com.hair.business.rest.MvcConstants.PAYMENT_URI;
import static com.hair.business.rest.MvcConstants.PAYPAL_URI_BASE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.services.payment.PaymentProcessor;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by olukoredeaguda on 10/02/2017.
 *
 * Paypal payment servelet
 */

@Path(PAYMENT_URI)
public class PaypalPaymentServlet {

    private final PaymentProcessor paymentProcessor;

    @Inject
    public PaypalPaymentServlet(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    @POST
    @Path(PAYPAL_URI_BASE)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response pay() {

        Properties props = System.getProperties();
        props.list(System.out);

        paymentProcessor.pay(1L, 43L, 54.34);
        return null;
    }
}
