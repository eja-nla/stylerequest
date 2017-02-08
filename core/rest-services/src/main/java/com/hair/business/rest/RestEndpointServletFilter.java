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
 *
 * Rest endpoint servlet filter
 */

public final class RestEndpointServletFilter extends GuiceFilter {

    private static final Logger log = Logger.getLogger(RestEndpointServletFilter.class.getName());
    static GitkitClient gitkitClient;

    private ServletContext context;

    private static final String loginUrl = System.getProperty("login.url");
    private static final String projectId = System.getProperty("project.id");
    private static final String gitkitUrl = System.getProperty("gitkit.url");
    private static final String clientId = System.getProperty("client.id");

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
                .setCookieName(System.getProperty("web.cookie.name")).build();
    }

    @Override
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
