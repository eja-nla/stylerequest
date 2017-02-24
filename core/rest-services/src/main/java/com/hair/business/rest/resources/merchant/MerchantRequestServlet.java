package com.hair.business.rest.resources.merchant;

import static com.hair.business.rest.MvcConstants.ACCEPT_REQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.CANCEL_REQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.COMPLETE_REQUEST_ENDPOINT;
import static com.hair.business.rest.MvcConstants.CREATE_MERCHANT_ENDPOINT;
import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.MERCHANT_URI;
import static com.hair.business.rest.MvcConstants.PUBLISH_STYLE_ENDPOINT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.identitytoolkit.GitkitUser;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.rest.resources.AbstractRequestServlet;
import com.hair.business.services.StyleRequestService;
import com.hair.business.services.StyleService;
import com.hair.business.services.merchant.MerchantService;
import com.x.business.utilities.Assert;

import java.util.List;

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
    public Response createMerchant(@Context HttpServletRequest request, Merchant merchant) {
        Assert.notNull(merchant, request);
        GitkitUser user = (GitkitUser) request.getAttribute("user");
        Assert.notNull(user);

        merchant.setEmail(user.getEmail());
        merchant.setFirstName(user.getName());
        merchant.setPhotoUrl(user.getPhotoUrl());

        merchantService.updateMerchant(merchant);
        return Response.ok().entity(merchant).build();
    }

    @POST
    @Path(PUBLISH_STYLE_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response publishStyle(List<Image> styleImages, @QueryParam("styleName") String styleName, @QueryParam("duration") int estimatedDuration, @QueryParam("merchantId") Long merchantId) {
        try {
            return Response.ok(styleService.publishStyle(styleName, estimatedDuration, merchantId, styleImages), MediaType.APPLICATION_JSON_TYPE).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

    }

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
