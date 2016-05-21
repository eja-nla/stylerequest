package com.hair.business.rest.config;

import com.hair.business.rest.DropwizardConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Rest Java Config
 *
 * Created by Olukorede Aguda on 30/04/2016.
 */
@Configuration
@ComponentScan(basePackages = {"com.hair.business.rest"})
public class RestConfiguration{

//   @Bean
//    public ObjectMapper mapper(){
//       ObjectMapper mapper = new ObjectMapper();
//       mapper.writerWithDefaultPrettyPrinter();
//       mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//       mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//       return mapper;
//    }

    @Bean
    public DropwizardConfiguration dropwizardConfiguration(){
        return new DropwizardConfiguration();
    }
}
