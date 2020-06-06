package com.hair.business.rest;

import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.inject.servlet.GuiceFilter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Olukorede Aguda on 25/06/2016.
 *
 * Rest endpoint servlet filter
 */

public final class RestEndpointServletFilter extends GuiceFilter {

    private static final Logger log = Logger.getLogger(RestEndpointServletFilter.class.getName());

    private static final String loginUrl = System.getProperty("login.url");
    private static final String userSessionName = System.getProperty("session.cookie.name");
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(System.getProperty("firebase.appname")));

    public RestEndpointServletFilter(){
    }

    public RestEndpointServletFilter(FirebaseAuth firebaseAuth){
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String sessionCookie = null;

        HttpServletRequest request = ((HttpServletRequest) servletRequest);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cooky : cookies) {
                if (cooky.getName().equals(userSessionName)) {
                    sessionCookie = cooky.getValue();
                }
            }
        }

        if (sessionCookie == null && request.getServletPath().endsWith("/auth0/sessionLogin")){
            filterChain.doFilter(servletRequest, servletResponse); // they're on their way to acquiring one, let them go. May need review.
            return;
        }

        if (sessionCookie == null && !servletResponse.isCommitted()){
            HttpServletResponse response = ((HttpServletResponse) servletResponse);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Access denied. Please login.");
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
            // Session cookie is unavailable, invalid or revoked. Go away
            HttpServletResponse response = ((HttpServletResponse) servletResponse);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(e.getMessage());
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
