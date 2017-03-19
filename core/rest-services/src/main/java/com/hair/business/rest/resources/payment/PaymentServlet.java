package com.hair.business.rest.resources.payment;

import static com.hair.business.rest.MvcConstants.BRAINTREE_AUTHORIZE_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.BRAINTREE_CAPTURE_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.PAYMENT_URI;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.StyleRequest;
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
public class PaymentServlet extends AbstractRequestServlet {

    private final PaymentService paymentService;

    @Inject
    public PaymentServlet(PaymentService PaypalPaymentProcessor) {
        this.paymentService = PaypalPaymentProcessor;
    }

    @POST
    @Path(BRAINTREE_AUTHORIZE_URI_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response authorisePayment(StyleRequest styleRequest, Customer customer) {

        try {
            StyleRequest payment = paymentService.holdPayment(styleRequest, customer);
            return Response.ok(payment).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

    }

    @POST
    @Path(BRAINTREE_CAPTURE_URI_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response captureAuthorisedPayment(Long stylerequestId, double total) {

        try {
            StyleRequest payment = paymentService.deductPreAuthPayment(stylerequestId, total);
            return Response.ok(payment).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

    }
}
