package com.hair.business.app;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.hair.business.app.provider.JacksonJsonProvidersProvider;
import com.hair.business.app.provider.ObjectMapperProvider;
import com.hair.business.app.servlets.HealthcheckServlet;
import com.hair.business.auth.config.SecurityModule;
import com.hair.business.dao.datastore.config.DaoDatastoreModule;
import com.hair.business.rest.RestServicesModule;
import com.hair.business.services.config.ServicesModule;
import com.x.business.config.UtilModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;

/**
 * Created by Olukorede Aguda on 23/05/2016.
 *
 *
 */
public class AppConfigurationMain extends GuiceServletContextListener {

    private static final Logger log = Logger.getLogger(AppConfigurationMain.class.getName());

    /**
     * Main application entry point.
     *
     * */
    protected Injector getInjector() {
        Injector appInjector = Guice.createInjector(new DaoDatastoreModule(), new HealthcheckModule(), new RestServicesModule(),
                    new ServicesModule(), new SecurityModule(), new UtilModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    Properties props = loadProperties();
                    log.info("found properties " + props.propertyNames());
                    Names.bindProperties(binder(), props);

                    // Jackson
                    bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
                    bind(JacksonJsonProvider.class).toProvider(JacksonJsonProvidersProvider.class).in(Singleton.class);

                }
            }

        );

        log.info("Application initialization completed successfully");

        return appInjector;
    }

    private Properties loadProperties() {

        Properties properties = new Properties();

        try {
            InputStream is = new FileInputStream(new File("/WEB-INF/config.properties"));
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

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        long time = System.currentTimeMillis();

        super.contextInitialized(servletContextEvent);

        long millis = System.currentTimeMillis() - time;

        log.info("Guice initialization took " + millis + " millis");
    }
}
