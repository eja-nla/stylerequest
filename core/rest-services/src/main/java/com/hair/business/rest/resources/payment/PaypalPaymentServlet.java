package com.hair.business.rest.resources.payment;

import static com.hair.business.rest.MvcConstants.PAYMENT_URI;
import static com.hair.business.rest.MvcConstants.PAYPAL_AUTHORIZE_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.PAYPAL_CAPTURE_URI_ENDPOINT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.payment.PaymentService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by olukoredeaguda on 10/02/2017.
 *
 * Ideally, this shouldn't be a servlet but on a second thought, it would be useful
 * if we ever have a need to manually reconcile certain payments e.g. to re
 *
 * Paypal payment servlet
 */

@Path(PAYMENT_URI)
public class PaypalPaymentServlet extends AbstractRequestServlet {

    private final PaymentService paypalPaymentService;

    @Inject
    public PaypalPaymentServlet(PaymentService PaypalPaymentProcessor) {
        this.paypalPaymentService = PaypalPaymentProcessor;
    }

    @POST
    @Path(PAYPAL_AUTHORIZE_URI_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response authorisePayment(StyleRequest styleRequest, Customer customer) {

        try {
            StyleRequestPayment payment = paypalPaymentService.holdPayment(styleRequest, customer);
            return Response.ok(payment).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

    }

    @POST
    @Path(PAYPAL_CAPTURE_URI_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response captureAuthorisedPayment(String authorizationId, double total, boolean isFinalCapture) {

        try {
            StyleRequestPayment payment = paypalPaymentService.deductPayment(authorizationId, total, isFinalCapture);
            return Response.ok(payment).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

    }
}
