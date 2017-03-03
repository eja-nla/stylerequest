package com.hair.business.rest;

import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.googlecode.objectify.ObjectifyFilter;
import com.hair.business.rest.provider.JacksonJsonProvidersProvider;
import com.hair.business.rest.provider.ObjectMapperProvider;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.model.AbstractResource;
import com.sun.jersey.api.model.AbstractSubResourceMethod;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.server.impl.modelapi.annotation.IntrospectionModeller;

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

    private static final String API_ENDPOINT = "/api/v1/*";
    private static final String RESOURCE_PACKAGES = "com.hair.business.rest.resources";

    private ServletContext servletContext;
    private final Map<String, String> endpoints = new HashMap<>();

    public RestServicesModule() {
        this.servletContext = this.getServletContext();
    }

    @Override
    protected void configureServlets() {

        ResourceConfig rc = new PackagesResourceConfig(RESOURCE_PACKAGES);
        endpoints.put("/health", "GET");
        for (Class clazz : rc.getClasses()) {
            this.bind(clazz); // Register jersey resources
            exposeServletEndpoints(clazz);
        }

        serve(API_ENDPOINT).with(GuiceContainer.class);
        filter(API_ENDPOINT).through(ObjectifyFilter.class);

        bind(ObjectifyFilter.class).in(Singleton.class);

        bind(RestEndpointServletFilter.class).toInstance(new RestEndpointServletFilter(servletContext));

        // Jackson
        bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
        bind(MessageBodyReader.class).to(JacksonJsonProvider.class).in(Singleton.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class).in(Singleton.class);
        Map<String, String> initParams = new HashMap<>();
        initParams.put("com.sun.jersey.config.feature.Trace", "true");
        initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        bind(JacksonJsonProvider.class).toProvider(JacksonJsonProvidersProvider.class).in(Singleton.class);

    }

    // exposed for testing purposes
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Singleton
    @Provides
    Map<String, String> provideMap() {
        return endpoints;
    }

    private void exposeServletEndpoints(Class resourceClass) {
        AbstractResource resource = IntrospectionModeller.createResource(resourceClass);
        String uriPrefix = resource.getPath().getValue();
        for (AbstractSubResourceMethod srm :resource.getSubResourceMethods()) {
            String uri = uriPrefix + srm.getPath().getValue();
            endpoints.put(uri, srm.getHttpMethod());
        }
    }
}
