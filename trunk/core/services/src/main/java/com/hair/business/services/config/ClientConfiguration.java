package com.hair.business.services.config;

import com.hair.business.services.client.HairWebclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Client configuration
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
@Configuration
public class ClientConfiguration {

    @Bean
    public HairWebclient createClient(@Value("${whateverUrl") String url){
        return new HairWebclient(); //TODO implement clients
    }
}
