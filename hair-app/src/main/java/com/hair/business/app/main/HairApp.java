package com.hair.business.app.main;

import com.hair.business.app.config.HairConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Olukorede Aguda on 25/04/2016.
 */
public class HairApp {
    static final Logger logger = LoggerFactory.getLogger(HairApp.class);

    public static void main(String[] args) {
        logger.info("Starting hair application");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(HairConfiguration.class);
        context.registerShutdownHook();
        context.refresh();
    }
}
