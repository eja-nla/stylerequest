package com.hair.business.rest.resources.style;

import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.STYLE_URI;
import static com.hair.business.rest.MvcConstants.UPDATE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.beans.entity.Style;
import com.hair.business.services.StyleService;
import com.x.business.utilities.Assert;

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
@Path(STYLE_URI)
public class StyleServlet {

    private final StyleService styleService;
    private final String NULL_MESSAGE = "Incoming object cannot be null";
    private final String NOT_FOUND_MESSAGE = "Style with ID %s not found";

    @Inject
    public StyleServlet(StyleService styleService) {
        this.styleService = styleService;
    }

    @GET
    @Path(INFO)
    @Produces(APPLICATION_JSON)
    public Response getStyleInfo(@Context HttpServletRequest request, @QueryParam(ID) Long styleId) {
        try {
            Assert.validId(styleId);
            Style style = styleService.findStyle(styleId);
            if (style == null) {
                return Response.status(Response.Status.NOT_FOUND).entity(NULL_MESSAGE).build();
            }

            return Response.ok(style, MediaType.APPLICATION_JSON).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @POST
    @Path(UPDATE)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response updateStyle(Style style) {

        try {
            Assert.notNull(style, "Style cannot be null.");
            Assert.validId(style.getId());
            styleService.updateStyle(style);

        } catch (IllegalArgumentException e){

            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        return Response.ok(style, MediaType.APPLICATION_JSON).build();
    }

}
