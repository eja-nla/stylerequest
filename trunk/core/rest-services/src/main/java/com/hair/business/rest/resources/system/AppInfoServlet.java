package com.hair.business.rest.resources.system;

import static com.hair.business.rest.MvcConstants.ADMIN_URI;
import static com.hair.business.rest.MvcConstants.GET_APP_INFO_PATH;
import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;

import com.google.firebase.auth.FirebaseToken;

import com.hair.business.rest.resources.AbstractRequestServlet;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import java.util.Map;

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
@Path(ADMIN_URI)
public class AppInfoServlet extends AbstractRequestServlet {

    private Map<Integer, Pair<String, String>> endpoints;
    private static final Logger logger = getLogger(AppInfoServlet.class);

    @Inject
    public AppInfoServlet(Provider<Map<Integer, Pair<String, String>>> endpoints) {
        this.endpoints = endpoints.get();
    }

    @GET
    @Path(GET_APP_INFO_PATH)
    @Produces(APPLICATION_JSON)
    public Response listEndpoints(@Context HttpServletRequest servletRequest) {
        logger.info(String.format("dumping endpoints for user %s", ((FirebaseToken) servletRequest.getAttribute(REST_USER_ATTRIBUTE)).getEmail()));
        return Response.ok(endpoints, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/version/info")
    @Produces(APPLICATION_JSON)
    public Response listVersionInfo(@Context HttpServletRequest servletRequest) {
        return Response.ok("0.0.1-SNAPSHOT", MediaType.APPLICATION_JSON).build();
    }


}
