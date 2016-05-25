package com.hair.business.rest;

import com.google.inject.servlet.ServletModule;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

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

    @Override
    protected void configureServlets() {

        ResourceConfig rc = new PackagesResourceConfig("com.hair.business.rest.resources");
        for ( Class<?> resource : rc.getClasses() ) {
            bind( resource );
        }

        serve("/api/v1/*").with(GuiceContainer.class);

    }

}
