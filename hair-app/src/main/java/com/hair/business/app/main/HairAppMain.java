package com.hair.business.app.main;

import com.hair.business.app.config.HairConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 */
public class HairAppMain {
    static final Logger logger = LoggerFactory.getLogger(HairAppMain.class);

    public static void main(String[] args) throws Exception {

        AnnotationConfigWebApplicationContext rootCtx = new AnnotationConfigWebApplicationContext();

        rootCtx.register(HairConfiguration.class);

        rootCtx.registerShutdownHook();

        rootCtx.refresh();

        logger.info("Application started successfully");

    }
}
