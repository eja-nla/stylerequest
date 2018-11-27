package com.hair.business.rest.resources.payment;

import static com.hair.business.rest.MvcConstants.BRAINTREE_AUTHORIZE_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.BRAINTREE_CAPTURE_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.BRAINTREE_TOKEN_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.PAYMENT_URI;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.payment.PaymentService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    public PaymentServlet(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    @Path(BRAINTREE_AUTHORIZE_URI_ENDPOINT)
    @Produces(APPLICATION_JSON)
    public Response authorizePayment(@QueryParam("tk") String nonce, @QueryParam("srId") Long styleRequestId, @QueryParam("cId") Long customerId) {

        try {
            StyleRequest payment = paymentService.authorize(nonce, styleRequestId, customerId);
            return Response.ok(payment).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(BRAINTREE_CAPTURE_URI_ENDPOINT)
    @Produces(APPLICATION_JSON)
    public Response captureAuthorizedPayment(@QueryParam("srId") Long stylerequestId, @QueryParam("total") double total) {

        try {
            StyleRequest payment = paymentService.deductPreAuthPayment(stylerequestId, total);
            return Response.ok(payment).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(BRAINTREE_TOKEN_URI_ENDPOINT)
    @Produces(APPLICATION_JSON)
    public Response issueClientToken(@QueryParam("entityId") String entityId) {

        try {
            String token = paymentService.issueClientToken(entityId);
            return token == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(wrapString(token)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }
}
