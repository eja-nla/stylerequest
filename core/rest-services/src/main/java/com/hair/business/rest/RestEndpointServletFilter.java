package com.hair.business.rest;

import static com.hair.business.rest.RestServicesConstants.REST_USER_ATTRIBUTE;

import com.google.identitytoolkit.GitkitClient;
import com.google.identitytoolkit.GitkitClientException;
import com.google.identitytoolkit.GitkitUser;
import com.google.inject.servlet.GuiceFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Olukorede Aguda on 25/06/2016.
 */

public final class RestEndpointServletFilter extends GuiceFilter {

    private static final Logger log = Logger.getLogger(RestEndpointServletFilter.class.getName());
    static GitkitClient gitkitClient;

    private ServletContext context;
    private static final String loginUrl = "http://localhost:4567/"; // FIXME: 27/08/2016 use System properties
    private static final String projectId = "amyrrh-test1";
    private static final String gitkitUrl = "http://localhost:4567/gitkit";
    private static final String clientId = "363084678705-mt0m4svcp4vg6i7j47kkng6e881loshu.apps.googleusercontent.com";

    public RestEndpointServletFilter(){

    }

    public RestEndpointServletFilter(ServletContext ctx) {
        this();
        this.context = ctx;

        InputStream keyStream = context.getResourceAsStream("WEB-INF/amyrrh-test1-48c176ef2baa.p12");

        gitkitClient = new GitkitClient.Builder()
                .setGoogleClientId(clientId)
                .setProjectId(projectId)
                .setServiceAccountEmail(System.getProperty("service.account.email"))
                .setKeyStream(keyStream)
                .setWidgetUrl(gitkitUrl)
                .setCookieName("gtoken").build();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        GitkitUser gitkitUser = null;
        HttpServletResponse res = ((HttpServletResponse) servletResponse);
        try {
            gitkitUser = gitkitClient.validateTokenInRequest((HttpServletRequest) servletRequest);
            if (gitkitUser == null && !servletResponse.isCommitted()){
                res.sendRedirect(loginUrl);
                return;
            }
        } catch (GitkitClientException e) {
            res.sendError(401, e.getMessage());
            log.severe(e.getMessage());
        }
        servletRequest.setAttribute(REST_USER_ATTRIBUTE, gitkitUser);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
