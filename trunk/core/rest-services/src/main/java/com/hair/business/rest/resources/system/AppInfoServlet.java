package com.hair.business.rest.resources.system;

import static com.hair.business.rest.MvcConstants.GET_APP_INFO_PATH;
import static com.hair.business.rest.MvcConstants.SYSTEM_URI;
import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.rest.resources.AbstractRequestServlet;

import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Exposes basic app info data
 *
 * Created by Olukorede Aguda on 12/02/2017.
 */
@Path(SYSTEM_URI)
public class AppInfoServlet extends AbstractRequestServlet {

    private Map<String, String> endpoints;
    private static final Logger logger = Logger.getLogger(AppInfoServlet.class.getName());

    @Inject
    public AppInfoServlet(Provider<Map<String, String>> endpoints) {
        this.endpoints = endpoints.get();
    }

    @GET
    @Path(GET_APP_INFO_PATH)
    @Produces(APPLICATION_JSON)
    public Response listEndpoints(@Context HttpServletRequest servletRequest) {
        logger.info(String.format("dumping endpoints for user %s", servletRequest.getAttribute(REST_USER_ATTRIBUTE)));
        return Response.ok(endpoints, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/version/info")
    @Produces(APPLICATION_JSON)
    public Response listVersionInfo(@Context HttpServletRequest servletRequest) {
        return Response.ok("0.0.1-SNAPSHOT", MediaType.APPLICATION_JSON).build();
    }


}
