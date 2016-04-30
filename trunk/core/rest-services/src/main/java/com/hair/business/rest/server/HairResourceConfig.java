package com.hair.business.rest.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.services.marshaller.ObjectMapperProvider;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Hair Resource config.
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Named
public class HairResourceConfig extends ResourceConfig implements ApplicationContextAware {


    private final ObjectMapper mapper;

    private ApplicationContext context;

    @Inject
    public HairResourceConfig(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setApplicationContext(ApplicationContext applicationContext){
        this.context = applicationContext;
    }

    @PostConstruct
    public void init(){
        Map<String, Object> beanControllers = context.getBeansWithAnnotation(RestController.class);

        // register each controller for use by jersey
        beanControllers.values().forEach(controller -> register(controller));

        register(JacksonFeature.class);

        register(new ObjectMapperProvider(mapper));

    }
}
