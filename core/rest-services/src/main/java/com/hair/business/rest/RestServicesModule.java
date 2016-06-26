package com.hair.business.rest;

import com.google.inject.servlet.ServletModule;

import com.googlecode.objectify.ObjectifyFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import javax.inject.Singleton;
import javax.servlet.ServletContext;

/**
 * Rest services module.
 *
 * Scans the controller package for Resource classes and binds them automatically
 * It is important that all Rest APIs are added to the resources package
 * otherwise we don't discover them via autoscan
 *
 * Created by olukoredeaguda on 23/05/2016.
 */
public class RestServicesModule extends ServletModule {

    private final String API_ENDPOINT = "/api/v1/*";
    private final String RESOURCE_PACKAGES = "com.hair.business.rest.resources";

    private ServletContext servletContext;

    public RestServicesModule(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected void configureServlets() {

        ResourceConfig rc = new PackagesResourceConfig(RESOURCE_PACKAGES);

        for ( Class<?> resource : rc.getClasses() ) {
            bind( resource );
        }

        serve(API_ENDPOINT).with(GuiceContainer.class);

        filter(API_ENDPOINT).through(ObjectifyFilter.class);

        bind(ObjectifyFilter.class).in(Singleton.class);

        bind(RestEndpointServletFilter.class).toInstance(new RestEndpointServletFilter(servletContext));

    }

}
