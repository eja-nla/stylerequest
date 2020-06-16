package com.hair.business.rest.resources.style;

import static com.hair.business.rest.MvcConstants.ID;
import static com.hair.business.rest.MvcConstants.INFO;
import static com.hair.business.rest.MvcConstants.STYLE_URI;
import static com.hair.business.rest.MvcConstants.UPDATE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.beans.entity.GeoPointExt;
import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.impl.HairstyleElasticsearchRepositoryImpl;
import com.hair.business.rest.resources.AbstractRequestServlet;
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
public class StyleServlet extends AbstractRequestServlet {

    private final StyleService styleService;
    private final HairstyleElasticsearchRepositoryImpl hairstyleElasticsearchRepository;
    private final String NULL_MESSAGE = "Incoming object cannot be null";

    @Inject
    public StyleServlet(StyleService styleService, HairstyleElasticsearchRepositoryImpl hairstyleElasticsearchRepository) {
        this.styleService = styleService;
        this.hairstyleElasticsearchRepository = hairstyleElasticsearchRepository;
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

    @POST
    @Path("/geoStyles")
    @Produces(APPLICATION_JSON)
    public Response geoStyles(@QueryParam("lon") double lon, @QueryParam("lat") double lat, @QueryParam("radius") int radiusInKm, @QueryParam("pageSize") int pageSize) {
        /**
         * We will return pages of ~100 styles, preloading 20 at a time at the client side.*/
        try {
            Assert.isTrue(lon != 0, "Longitude cannot be zero");
            Assert.isTrue(lat != 0, "Latitude cannot be zero");
            Assert.isTrue(radiusInKm > 0, "Radius must be greater than zero");

            GeoPointExt geoPoint = new GeoPointExt(lat, lon);
            return Response.ok(styleService.geoSeachStyles(geoPoint, radiusInKm, pageSize==0?100:pageSize), MediaType.APPLICATION_JSON).build();
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/scroll")
    @Produces(APPLICATION_JSON)
    //the scroll API comes here
    //"scroll" : "1m",
    //"scroll_id" : "DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAAAD4WYm9laVYtZndUQlNsdDcwakFMNjU1QQ=="
    public Response fetchPages(@QueryParam("scroll") String scroll, @QueryParam("scrollId") String scrollId){
        try {
            Assert.notNull(scroll, "Scroll cannot be null");
            Assert.notNull(scrollId, "Scroll ID cannot be null");
            return Response.ok(styleService.scroll(scroll, scrollId), MediaType.APPLICATION_JSON).build();
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/blindquery")
    // We may need to run an arbitrary query against DB (utility use) so we leave it here.
    // Discouraged strongly for production use
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response blindRequest(String requestQuery, @QueryParam("endpoint") String endpoint, @QueryParam("httpmethod") String httpMethod) {

        if (httpMethod.equals("GET") || httpMethod.equals("POST") || httpMethod.equals("PUT")){
            return Response.ok(hairstyleElasticsearchRepository.blindQuery(requestQuery, endpoint, httpMethod), MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(wrapString("Requested Blind query not allowed.")).build();
        }
    }

}
