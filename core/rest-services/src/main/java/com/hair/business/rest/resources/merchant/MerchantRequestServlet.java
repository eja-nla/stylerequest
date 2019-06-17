package com.hair.business.rest.resources.merchant;

import static com.hair.business.rest.MvcConstants.ACCEPT_REQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.CANCEL_REQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.COMPLETE_REQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.CREATE_MERCHANT_ENDPOINT;
import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.MERCHANT_URI;
import static com.hair.business.rest.MvcConstants.PUBLISH_STYLE_ENDPOINT;
import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;

import com.google.firebase.auth.FirebaseToken;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.StyleRequestService;
import com.hair.business.services.StyleService;
import com.hair.business.services.merchant.MerchantService;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Merchant request controller.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Path(MERCHANT_URI)
public class MerchantRequestServlet extends AbstractRequestServlet {

    private final MerchantService merchantService;
    private final StyleService styleService;
    private final StyleRequestService styleRequestService;

    private static final Logger log = getLogger(MerchantRequestServlet.class);

    @Inject
    public MerchantRequestServlet(MerchantService merchantService, StyleService styleService, StyleRequestService styleRequestService) {
        this.merchantService = merchantService;
        this.styleService = styleService;
        this.styleRequestService = styleRequestService;
    }

    @GET
    @Path(INFO)
    @Produces(APPLICATION_JSON)
    public Response getMerchantInfo(@QueryParam(ID) Long merchantId) {
        try {
            Assert.validId(merchantId);

            return Response.ok(merchantService.findMerchant(merchantId)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(CREATE_MERCHANT_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createMerchant(@Context HttpServletRequest request, Merchant merchant, final @QueryParam("token") String nonce) {
        log.info("Creating new merchant '{}'", merchant.getEmail());

        try {
            FirebaseToken user = (FirebaseToken) request.getAttribute(REST_USER_ATTRIBUTE);
            Assert.notNull(user);

            String names[] = user.getName().split(" ", 2);
            merchant.setFirstName(names[0]);
            merchant.setLastName(names[1]);
            merchant.setEmail(user.getEmail());
            merchant.setPhotoUrl(user.getPicture());
            
            return Response.ok(wrapString(merchantService.createMerchant(merchant, nonce)), MediaType.APPLICATION_JSON_TYPE).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(PUBLISH_STYLE_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response publishStyle(Style style, @QueryParam("merchantId") Long merchantId) {
        try {
            //return Response.ok(styleService.publishStyle(styleName, estimatedDuration, merchantId, styleImages), MediaType.APPLICATION_JSON_TYPE).build();
            return Response.ok(styleService.publishStyle(style, merchantId), MediaType.APPLICATION_JSON_TYPE).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

    }

    /**
     * With current design, it's possible for a different merchant to accept a styleRequest
     * The alternative is to load the stylerequest's merchant and compare their email with this merchant's email (obtained via Gitkit)
     * That's costly (relative to the marginal benefit, given the chance of a different merchant accepting a request is actually low)
     * as we have to do a fetch of the stylerequest merchant
     * */
    @POST
    @Path(ACCEPT_REQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response acceptRequest(@QueryParam("stylerequestId") Long styleRequestId, Preferences preferences) {
        try {
            styleRequestService.acceptStyleRequest(styleRequestId, preferences);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

        return Response.ok().build();
    }

    @POST
    @Path(CANCEL_REQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response cancelRequest(@QueryParam("stylerequestId") Long styleRequestId, Preferences preferences) {
        Assert.validId(styleRequestId);

        try {
            styleRequestService.cancelStyleRequest(styleRequestId, preferences);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

        return Response.ok().build();
    }

    @POST
    @Path(COMPLETE_REQUEST_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response completeRequest(@QueryParam("stylerequestId") Long styleRequestId, Preferences preferences) {
        Assert.validId(styleRequestId);

        try {
            styleRequestService.completeStyleRequest(styleRequestId, preferences);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

        return Response.ok().build();
    }
}
