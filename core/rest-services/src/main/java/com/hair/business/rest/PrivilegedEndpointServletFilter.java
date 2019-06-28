package com.hair.business.rest;

import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;
import static org.slf4j.LoggerFactory.getLogger;

import com.google.firebase.auth.FirebaseToken;
import com.google.inject.servlet.GuiceFilter;

import org.slf4j.Logger;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Olukorede Aguda on 25/06/2016.
 *
 * Rest endpoint servlet filter
 */

public final class PrivilegedEndpointServletFilter extends GuiceFilter {

    private static final String[] whitelistedUsers = System.getProperty("app.whitelisted.users").split(",");
    private static final Logger logger = getLogger(PrivilegedEndpointServletFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        FirebaseToken user = (FirebaseToken) servletRequest.getAttribute(REST_USER_ATTRIBUTE);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Access denied. Please login.");
            logger.warn(String.format("Admin access denied for user %s", user.getEmail()));
            return;
        }

        boolean found = false;

        for (String whitelistedUser : whitelistedUsers) {
            if (user.getEmail().equals(whitelistedUser)) {
                found = true;
            }
        }
        if (!found) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Access denied, contact admin.");
            logger.warn(String.format("Admin access denied for user %s", user.getEmail()));
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
