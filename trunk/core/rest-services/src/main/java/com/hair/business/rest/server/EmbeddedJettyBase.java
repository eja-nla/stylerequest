package com.hair.business.rest.server;

import org.springframework.web.context.WebApplicationContext;

/**
 * Embedded Jetty server's base.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
public interface EmbeddedJettyBase {

    void start(int port, WebApplicationContext context) throws Exception;

    void stop() throws Exception;
}