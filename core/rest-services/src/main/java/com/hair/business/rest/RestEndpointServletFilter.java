package com.hair.business.rest;

import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.inject.servlet.GuiceFilter;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

/**
 * Created by Olukorede Aguda on 25/06/2016.
 *
 * Rest endpoint servlet filter
 */

public final class RestEndpointServletFilter extends GuiceFilter {

    private static final Logger log = Logger.getLogger(RestEndpointServletFilter.class.getName());

    private static final String loginUrl = System.getProperty("login.url");
    public static final String userSessionName = System.getProperty("session.cookie.name");
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(System.getProperty("firebase.appname")));

    public RestEndpointServletFilter(){
    }

    @Inject
    public RestEndpointServletFilter(Provider<FirebaseApp> firebaseAppProvider){
        this();
//        this.firebaseApp = firebaseAppProvider.get();
//        System.out.println("RestEndpointServletFilter(); " + firebaseApp);
    }

//    public RestEndpointServletFilter(ServletContext ctx) {
//        System.out.println("RestEndpointServletFilter(ctx);" + firebaseApp);
//    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String sessionCookie = null;

        HttpServletRequest request = ((HttpServletRequest) servletRequest);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(userSessionName)){
                    sessionCookie = cookies[i].getValue();
                }
            }
        }

        if (sessionCookie == null && request.getServletPath().endsWith("/auth0/sessionLogin")){
            filterChain.doFilter(servletRequest, servletResponse); // they're on their way to acquiring one, let them go. May need review.
            return;
        }

        if (sessionCookie == null && !servletResponse.isCommitted()){
            ((HttpServletResponse) servletResponse).sendRedirect(loginUrl);
            return;
        }
        try {
            // Verify the session cookie. In this case an additional check is added to detect
            // if the user's Firebase session was revoked, user deleted/disabled, etc.
            final boolean checkRevoked = true;
            FirebaseToken decodedToken = firebaseAuth.verifySessionCookie(sessionCookie, checkRevoked);
            servletRequest.setAttribute(REST_USER_ATTRIBUTE, decodedToken);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (FirebaseAuthException e) {
            // Session cookie is unavailable, invalid or revoked. Force user to login.
            Response.temporaryRedirect(URI.create(loginUrl)).build();
        }
    }
}
