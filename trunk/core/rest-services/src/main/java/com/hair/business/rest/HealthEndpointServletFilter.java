package com.hair.business.rest;

import com.google.inject.servlet.GuiceFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by Olukorede Aguda on 23/06/2019.
 *
 * Health endpoint servlet filter
 */

public final class HealthEndpointServletFilter extends GuiceFilter {

    public HealthEndpointServletFilter(){
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {

        String response = "{\"code\":200,\"message\":\"I'm healthy. I Hope you are?\"}";
        servletResponse.setContentType("application/json");
        servletResponse.getOutputStream().print(response);

    }
}
