package com.hair.business.rest;

import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;

import com.google.identitytoolkit.GitkitUser;
import com.google.inject.servlet.GuiceFilter;

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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        GitkitUser user = (GitkitUser) servletRequest.getAttribute(REST_USER_ATTRIBUTE);

        if (user == null) {
            response.sendError(401, "Access denied. Please login.");
            return;
        }

        boolean found = false;

        for (String whitelistedUser : whitelistedUsers) {
            if (user.getEmail().equals(whitelistedUser)) {
                found = true;
            }
        }
        if (!found) {
            response.sendError(401, "Access denied for non-whitelisted user " + user);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
