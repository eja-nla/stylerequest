package com.hair.business.rest.resources.auth;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;

import com.hair.business.beans.entity.nonPersist.LoginRequest;
import com.hair.business.rest.RestEndpointServletFilter;
import com.hair.business.rest.resources.AbstractRequestServlet;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * Created by Olukorede Aguda on 12/06/2019.
 */
@Path("/auth0")
public class FirebaseSessionLoginServlet extends AbstractRequestServlet {

    // see https://github.com/firebase/firebase-admin-java/blob/master/src/test/java/com/google/firebase/snippets/FirebaseAuthSnippets.java

    // Set session expiration to x days.
    private static final SessionCookieOptions options = SessionCookieOptions.builder().setExpiresIn(TimeUnit.DAYS.toMillis(10)).build();
    private static final String loginUrl = System.getProperty("login.url");
    private static final String userSessionName = System.getProperty("session.cookie.name");

    /**
     * Endpoint that must be called whenever the client signs in a new user so we can create their session cookie
     * */
    @POST
    @Path("/sessionLogin")
    @Consumes(APPLICATION_JSON)
    public Response createSessionCookie(LoginRequest request) {

        try {
            // Get the ID token sent by the client
            String idToken = request.getIdToken();
            if (idToken == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid/Null idToken").build();
            }
            // Create the session cookie. This will also verify the ID token in the process.
            // The session cookie will have the same claims as the ID token.
            String sessionCookie = RestEndpointServletFilter.firebaseAuth.createSessionCookie(idToken, options);
            // Set cookie policy parameters as required.
            final NewCookie cookie = new NewCookie(userSessionName, sessionCookie, "/", "", "SessionID", 864000, false);
            return Response.ok().cookie(cookie).build();
        } catch (FirebaseAuthException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Failed to create a session cookie").build();
        }
    }

    @POST
    @Path("/sessionLogout")
    public Response clearSessionCookie(@CookieParam("usersession") Cookie cookie) {
        String sessionCookie = cookie.getValue();
        try {
            FirebaseToken decodedToken = RestEndpointServletFilter.firebaseAuth.verifySessionCookie(sessionCookie);
            FirebaseAuth.getInstance().revokeRefreshTokens(decodedToken.getUid());
            NewCookie newCookie = new NewCookie(cookie, "Expired", 0, true);
            return Response.temporaryRedirect(URI.create(loginUrl)).cookie(newCookie).build();
        } catch (FirebaseAuthException e) {
            return Response.temporaryRedirect(URI.create(loginUrl)).build();
        }
    }

}
