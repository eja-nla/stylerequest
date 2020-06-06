package com.hair.business.rest.resources.system;

import static com.hair.business.rest.MvcConstants.ADMIN_URI;
import static com.hair.business.rest.MvcConstants.GET_APP_INFO_PATH;
import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;

import com.google.appengine.api.utils.SystemProperty;
import com.google.firebase.auth.FirebaseToken;

import com.hair.business.rest.resources.AbstractRequestServlet;

import org.slf4j.Logger;

import java.util.Map;
import java.util.TreeMap;

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

    private TreeMap<String, String> endpoints;
    private static final Logger logger = getLogger(AppInfoServlet.class);

    @Inject
    public AppInfoServlet(Provider<Map<String, String>> endpoints) {
        this.endpoints = new TreeMap<>(endpoints.get());
    }

    @GET
    @Path(GET_APP_INFO_PATH)
    @Produces(APPLICATION_JSON)
    public Response listEndpoints(@Context HttpServletRequest servletRequest) {
        logger.info(String.format("dumping endpoints for user %s", ((FirebaseToken) servletRequest.getAttribute(REST_USER_ATTRIBUTE)).getEmail()));
        return Response.ok(endpoints, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/info/version")
    @Produces(APPLICATION_JSON)
    public Response listVersionInfo(@Context HttpServletRequest servletRequest) {
        return Response.ok(SystemProperty.version.get(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/info/details")
    @Produces(APPLICATION_JSON)
    public Response listAppDetails(@Context HttpServletRequest servletRequest) {
        return Response.ok(SystemProperty.applicationId.get() + "\n" + SystemProperty.applicationVersion.get() + "\n" + SystemProperty.environment.get(), MediaType.APPLICATION_JSON).build();
    }


}
