package com.hair.business.rest.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

import javax.inject.Named;

/**
 * Embedded Jetty implementation.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Named
public class EmbeddedJettyBaseImpl implements  EmbeddedJettyBase {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";
    private static final String WEBAPP_DIR = "webapp";

    Server server;

    @Override
    public void start(int port, WebApplicationContext context) throws Exception {
        logger.info("Initializing servlet container...");
        server = new Server(port);

        server.setHandler(getServletContextHandler(context));
        server.start();
        server.join();
    }

    @Override
    public void stop() throws Exception {

        server.stop();
    }

    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();

        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(context));
        contextHandler.setResourceBase(new ClassPathResource(WEBAPP_DIR).getURI().toString());

        return contextHandler;
    }


}
