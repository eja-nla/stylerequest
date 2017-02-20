package com.hair.business.rest.resources.merchant;

import static com.hair.business.rest.MvcConstants.CREATE_MERCHANT_ENDPOINT;
import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.MERCHANT_URI;
import static com.hair.business.rest.MvcConstants.PUBLISH_STYLE_ENDPOINT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.identitytoolkit.GitkitUser;

import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.rest.resources.AbstractRequestServlet;
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

    @Inject
    public MerchantRequestServlet(MerchantService merchantService, StyleService styleService) {
        this.merchantService = merchantService;
        this.styleService = styleService;
    }

    @GET
    @Path(INFO)
    @Produces(APPLICATION_JSON)
    public Response getMerchantInfo(@QueryParam(ID) Long merchantId) {
        Assert.validId(merchantId);
        try {
            return Response.ok(merchantService.findMerchant(merchantId)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path(CREATE_MERCHANT_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createMerchant(@Context HttpServletRequest request, Merchant merchant) {
        GitkitUser user = (GitkitUser) request.getAttribute("user");

        merchant.setEmail(user.getEmail());
        merchant.setFirstName(user.getName());
        merchant.setPhotoUrl(user.getPhotoUrl());
        merchantService.updateMerchant(merchant);
        return Response.ok().build();
    }

    @POST
    @Path(PUBLISH_STYLE_ENDPOINT)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Style publishStyle(List<Image> styleImages, @QueryParam("styleName") String styleName, @QueryParam("duration") int estimatedDuration, @QueryParam("merchantId") Long merchantId) {
        return styleService.publishStyle(styleName, estimatedDuration, merchantId, styleImages);
    }

}
