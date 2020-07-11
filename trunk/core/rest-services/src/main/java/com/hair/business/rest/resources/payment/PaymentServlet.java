package com.hair.business.rest.resources.payment;

import static com.hair.business.rest.MvcConstants.PAYMENT_URI;
import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;

import com.google.firebase.auth.FirebaseToken;

import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.TransactionResult;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.payment.stripe.StripePaymentService;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
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

    private static final Logger logger = getLogger(PaymentServlet.class);


    private final StripePaymentService stripe;
    private final Repository repository;

    @Inject
    public PaymentServlet(StripePaymentService stripe, Repository repository) {
        this.stripe = stripe;
        this.repository = repository;
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
    @Path("/onboard/redirect")
    @Produces(APPLICATION_JSON)
    public Response createMerchantPaymentProfile(@QueryParam("code") String authCodeFromStripeAfterSignup, @QueryParam("state") String refId) {
        try {
            stripe.createMerchant(authCodeFromStripeAfterSignup, refId.split("_")[1]);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    /**
     * Create new stripe customer
     * */
    @POST
    @Path("/onboard/customer")
    @Produces(APPLICATION_JSON)
    public Response createCustomerPaymentProfile(@QueryParam("id") String internalCustomerId) {
        try {
            String stripeID = stripe.createCustomer(internalCustomerId);
            Customer customer = repository.findOne(Long.valueOf(internalCustomerId), Customer.class);
            Assert.notNull(customer, String.format("Customer with id %s not found", internalCustomerId));

            customer.setPaymentId(stripeID);
            repository.saveOne(customer);

            return Response.ok(wrapString(stripeID)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    /**
     *
     * */
    @POST
    @Path("/authorize")
    @Produces(APPLICATION_JSON)
    public Response authorizePayment(@QueryParam("srId") Long styleRequestId, @QueryParam("description") Long description) {

        try {
            TransactionResult result = stripe.authorize(styleRequestId, "Authorization for style " + description);
            String message = result.getStatusMessage().equals("succeeded") ?
                    String.format("Successfully refunded %s", result.getTotalAmount()) :
                    String.format("Failed to refund for StyleRequest %s", styleRequestId);

            return Response.ok(wrapString(message)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    /**
     * Refund for this StyleRequest.
     *
     * If the requester is not the merchant, deny the request
     *
     * If partial refund, send the list of addons to refund.
     * If full refund, send a null/empty addon list
     * */
    @POST
    @Path("/refund/stylerequest")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response refundStyleRequest(@Context HttpServletRequest request, @QueryParam("srId") Long stylerequestId, @QueryParam("amount") int amount, List<AddOn> addOns) {

        try {

            final FirebaseToken userDetails = (FirebaseToken) request.getAttribute(REST_USER_ATTRIBUTE);
            final StyleRequest styleRequest = repository.findOne(stylerequestId, StyleRequest.class);

            Assert.notNull(styleRequest, String.format("StyleRequest with id %s not found", stylerequestId));

            // validate the requester is the merchant
            if (userDetails != null && userDetails.getEmail().equals(styleRequest.getMerchant().getEmail())){
                logger.warn("StyleRequest ID={} refund denied for merchant email {}", stylerequestId, userDetails.getEmail());
                return Response.status(Response.Status.UNAUTHORIZED).entity(wrapString("Not authorized to issue this refund")).build();
            }

            final TransactionResult result = stripe.refund(styleRequest, amount, addOns);

            // failed
            if (!result.getStatusMessage().equals("succeeded")) {
                return Response.status(Response.Status.NOT_MODIFIED).entity(wrapString("Refund operation failed")).build();
            }

            // success
            return Response.ok(wrapString(result.getStatusMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        }
    }
}
