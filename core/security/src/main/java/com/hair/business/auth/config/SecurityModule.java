package com.hair.business.auth.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.inject.AbstractModule;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 */
public class SecurityModule extends AbstractModule {

    private static final Logger log = Logger.getLogger(SecurityModule.class.getName());

    protected void configure() {

        GoogleCredential credential = null;
        // auth stuff
        try {
            credential = GoogleCredential.getApplicationDefault();

        } catch (IOException e) {
            log.severe(e.getMessage());
        }

        install(this);
    }
}
