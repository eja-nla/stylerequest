package com.hair.business.app;

import static org.slf4j.LoggerFactory.getLogger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;

import com.hair.business.auth.config.SecurityModule;
import com.hair.business.dao.datastore.config.DaoDatastoreModule;
import com.hair.business.rest.RestServicesModule;
import com.hair.business.services.config.ServicesModule;
import com.x.business.config.UtilModule;

import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Olukorede Aguda on 23/05/2016.
 *
 *
 */
public class AppConfigurationMain extends GuiceServletContextListener {

    private static final Logger log = getLogger(AppConfigurationMain.class);

    /**
     * Main application entry point.
     *
     * */
    @Override
    protected Injector getInjector() {
        log.info("Application initialization starting...");
        Injector appInjector = Guice.createInjector(new DaoDatastoreModule(), new RestServicesModule(),
                    new ServicesModule(), new SecurityModule(), new UtilModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    Properties props = loadProperties();
                    log.info("found properties {}", props.propertyNames());
                    Names.bindProperties(binder(), props);

                    install(this);
                }
            }

        );

        log.info("Application initialization completed successfully");

        return appInjector;
    }

    private Properties loadProperties() {

        Properties properties = new Properties();

        try {
            InputStream is = new FileInputStream(new File("WEB-INF/config.properties"));
            properties.load(is);

        } catch (IOException e) {
            log.error("Failed to load properties file with errors: " + e.getMessage());
        }

        return properties;
    }
}
