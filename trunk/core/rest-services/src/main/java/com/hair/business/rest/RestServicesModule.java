package com.hair.business.rest;

import com.google.inject.servlet.ServletModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.googlecode.objectify.ObjectifyFilter;
import com.hair.business.rest.provider.JacksonJsonProvidersProvider;
import com.hair.business.rest.provider.ObjectMapperProvider;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

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

    public RestServicesModule() {
        this.servletContext = this.getServletContext();
    }

    @Override
    protected void configureServlets() {

        ResourceConfig rc = new PackagesResourceConfig(RESOURCE_PACKAGES);
        // Register jersey resources
        rc.getClasses().forEach(this::bind);

        serve(API_ENDPOINT).with(GuiceContainer.class);
        filter(API_ENDPOINT).through(ObjectifyFilter.class);

        bind(ObjectifyFilter.class).in(Singleton.class);

        bind(RestEndpointServletFilter.class).toInstance(new RestEndpointServletFilter(servletContext));

        // Jackson
        bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);
        Map<String, String> initParams = new HashMap<>();
        initParams.put("com.sun.jersey.config.feature.Trace", "true");
        initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        bind(JacksonJsonProvider.class).toProvider(JacksonJsonProvidersProvider.class).in(Singleton.class);
    }

    // exposed for testing purposes
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
