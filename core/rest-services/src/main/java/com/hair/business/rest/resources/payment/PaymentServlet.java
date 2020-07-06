package com.hair.business.rest.resources.payment;

import static com.hair.business.rest.MvcConstants.BRAINTREE_AUTHORIZE_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.BRAINTREE_REFUND_SR_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.BRAINTREE_REFUND_TX_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.BRAINTREE_TOKEN_URI_ENDPOINT;
import static com.hair.business.rest.MvcConstants.PAYMENT_URI;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.payment.PaymentService;

import java.math.BigDecimal;

import javax.annotation.Nullable;
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

    /**
     * The flow to create a connected account is detailed here
     * https://stripe.com/docs/connect/express-accounts#onboarding-express-accounts-outside-of-your-platforms-country
     *
     * Summarized as follows
     *
     * Phase1  (Create user internally)
     *      Mobile app will
     *          Take few details of merchant and create their account (name, email)
     *          Post the details to backend to create merchant. Backend creates the merchant and returns merchant json to mobile app, with a merchant ID
     *          Note the merchantId and generate a new referenceID like "somePrefix_<merchantId>". Prefix used for decoy
     *
     * Phase2   (Create user externally at Stripe)
     *      Mobile app will call a stripe connect link generated in step1 of above doc. That link *must* have a state parameter whose value is the earlier generated referenceID
     *          e.g. https://connect.stripe.com/express/oauth/authorize?redirect_uri=https://www.ourOwnUrl.com/admin/payment/onboard/redirect&state=referenceID&client_id=<ca_something></>&state={STATE_VALUE}&suggested_capabilities[]=transfers
     *      Merchant will fill details required by Stripe
     *      After above step (step3 of the onboarding doc), Stripe hits backend to notify us of account creation (new stylist/merchant)
     *          this will come with an AUTHORIZATION_CODE as well as the referenceID we passed earlier (state=referenceID)
     *
     * Phase3   (Finish the process internally)
     *      Backend will
     *          Split referenceID (which is "somePrefix_<merchantId>") into "somePrefix_" and "merchantId"
     *          Send request to Stripe with the AUTHORIZATION_CODE and obtain the stripeID of the newly created merchant/connected account
     *          Lookup merchant with ID=merchantId, if found (should be), update the paymentId of that user with the new stripeID
     *          Save.
     * */
    @POST
    @Path("onboard/redirect")
    @Produces(APPLICATION_JSON)
    public Response createPaymentProfile(@QueryParam("code") String authCodeFromStripeAfterSignup, @QueryParam("state") String refId) {
        try {
            paymentService.createMerchantProfile(authCodeFromStripeAfterSignup, refId.split("_")[1]);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(BRAINTREE_AUTHORIZE_URI_ENDPOINT)
    @Produces(APPLICATION_JSON)
    public Response authorizePayment(@QueryParam("tk") String nonce, @QueryParam("srId") Long styleRequestId, @QueryParam("cId") Long customerId) {

        try {
            StyleRequest payment = paymentService.authorize(nonce, styleRequestId);
            return Response.ok(payment).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(BRAINTREE_REFUND_SR_URI_ENDPOINT)
    @Produces(APPLICATION_JSON)
    public Response refundStyleRequest(@QueryParam("srId") Long stylerequestId, @QueryParam("amount") double amount) {

        try {
            paymentService.refund(stylerequestId, amount);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(BRAINTREE_REFUND_TX_URI_ENDPOINT)
    @Produces(APPLICATION_JSON)
    public Response refundTransaction(@QueryParam("trId") String transactionId, @QueryParam("amount") double amount) {

        try {
            paymentService.refund(transactionId, BigDecimal.valueOf(amount));
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(BRAINTREE_TOKEN_URI_ENDPOINT)
    @Produces(APPLICATION_JSON)
    public Response issueClientToken(@QueryParam("entityId") @Nullable String entityId) {

        try {
            String token = paymentService.issueClientToken(entityId);
            return token == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(wrapString(token)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }
}
