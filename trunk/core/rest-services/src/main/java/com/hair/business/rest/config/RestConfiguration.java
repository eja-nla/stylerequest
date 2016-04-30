package com.hair.business.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.rest.server.EmbeddedJettyBase;
import com.hair.business.rest.server.EmbeddedJettyBaseImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Rest Java Config
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Configuration
@ComponentScan("com.hair.business.rest")
@EnableWebMvc
public class RestConfiguration {

    private static final String CONFIG_LOCATION = "com.hair.business.rest";
    private static final String DEFAULT_PROFILE = "dev";

    @Bean
    public ObjectMapper mapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter();
        return mapper;
    }

    @Bean
    public EmbeddedJettyBase getWebapp(@Value("${hair.webapp.base}") String webappBase) throws Exception{
        EmbeddedJettyBase webapp = new EmbeddedJettyBaseImpl();

        webapp.start(18080, getContext());

        System.out.println("done");

        return webapp;
    }


    private static WebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        context.setConfigLocation(CONFIG_LOCATION);
        context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);

        return context;
    }

}
