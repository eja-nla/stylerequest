package com.hair.business.rest.resources.merchant;

import static com.hair.business.rest.MvcConstants.CREATE;
import static com.hair.business.rest.MvcConstants.EMAIL;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.MERCHANT_URI;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.identitytoolkit.GitkitUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.services.customer.MerchantService;

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
public class MerchantRequestServlet {

    private final MerchantService merchantService;
    private final ObjectMapper mapper;

    @Inject
    public MerchantRequestServlet(MerchantService merchantService, ObjectMapper mapper) {
        this.merchantService = merchantService;
        this.mapper = mapper;
    }

    @GET
    @Path(INFO)
    @Produces(APPLICATION_JSON)
    public String getMerchantInfo(@Context HttpServletRequest request, @QueryParam(EMAIL) Long customerEmail) {
        GitkitUser user = (GitkitUser) request.getAttribute("user");
        return "hey " + customerEmail + request.getHeader("gtoken");
    }

    @POST
    @Path(CREATE)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createNewMerchant(@Context HttpServletRequest request, Merchant merchant) {
        GitkitUser user = (GitkitUser) request.getAttribute("user");

        merchant.setEmail(user.getEmail());
        merchant.setName(user.getName());
        merchant.setPhotoUrl(user.getPhotoUrl());
        merchantService.updateMerchant(merchant);
        return Response.ok().build();
    }
}
