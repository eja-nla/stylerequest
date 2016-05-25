package com.hair.business.app;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import com.hair.business.app.servlets.HealthcheckServlet;
import com.hair.business.dao.datastore.config.DaoDatastoreModule;
import com.hair.business.rest.RestServicesModule;
import com.hair.business.services.config.ServicesModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 23/05/2016.
 *
 *
 */
public class GuiceServletConfig extends GuiceServletContextListener {

    private static final Logger log = Logger.getLogger(GuiceServletConfig.class.getName());

    /**
     * Main application entry point.
     *
     * */
    protected Injector getInjector() {

        Injector appInjector = Guice.createInjector(new HealthcheckModule(), new RestServicesModule(),
                new DaoDatastoreModule(), new ServicesModule(), new AbstractModule() {
                    @Override
                    protected void configure() {
                        Properties props = loadProperties();
                        log.info("found properties " + props.propertyNames());
                        Names.bindProperties(binder(), props);
                    }
                }
        );

        log.info("Application initialisation completed successfully");

        return appInjector;
    }

    private static Properties loadProperties() {

        Properties properties = new Properties();

        try {
            InputStream is = new FileInputStream(new File("WEB-INF/config.properties"));
            properties.load(is);

        } catch (IOException e) {
            log.severe("Failed to load properties file.");
        }

        return properties;
    }

    //TODO move into own class
    private class HealthcheckModule extends ServletModule {
        @Override
        protected void configureServlets() {
            serve("/health").with(HealthcheckServlet.class);

        }
    }
}
