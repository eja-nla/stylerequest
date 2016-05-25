package com.hair.business.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import com.hair.business.app.servlets.HealthcheckServlet;
import com.hair.business.dao.datastore.config.DaoDatastoreModule;
import com.hair.business.rest.RestServicesModule;
import com.hair.business.services.config.ServicesModule;

/**
 * Created by Olukorede Aguda on 23/05/2016.
 *
 *
 */
public class GuiceServletConfig extends GuiceServletContextListener {

    //TODO move into own class
    private class HealthcheckModule extends ServletModule {
        @Override
        protected void configureServlets() {
            serve("/health").with(HealthcheckServlet.class);

        }
    }

    /**
     * Main application entry point.
     *
     * */
    protected Injector getInjector() {

        return Guice.createInjector(new HealthcheckModule(), new RestServicesModule(),
                new DaoDatastoreModule(), new ServicesModule());
    }
}
