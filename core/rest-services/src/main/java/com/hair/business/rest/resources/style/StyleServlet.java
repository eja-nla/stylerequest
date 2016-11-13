package com.hair.business.rest.resources.style;

import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.STYLE_URI;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.beans.entity.Style;
import com.hair.business.services.customer.StyleService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
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
@Path(STYLE_URI)
public class StyleServlet {

    private final StyleService styleService;

    @Inject
    public StyleServlet(StyleService styleService) {
        this.styleService = styleService;
    }

    @GET
    @Path(INFO)
    @Produces(APPLICATION_JSON)
    public Response getStyleInfo(@Context HttpServletRequest request, @QueryParam(ID) Long styleId) {
        Style style = styleService.getStyleInfo(styleId);

        if (style == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Style not found").build();
        }

        return Response.ok(style, MediaType.APPLICATION_JSON).build();
    }

}
