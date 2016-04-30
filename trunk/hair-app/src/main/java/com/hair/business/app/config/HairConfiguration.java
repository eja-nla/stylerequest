package com.hair.business.app.config;

import com.hair.business.cache.config.CacheConfiguration;
import com.hair.business.dao.es.config.DaoEsConfiguration;
import com.hair.business.rest.config.RestConfiguration;
import com.hair.business.services.config.ServiceConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Hair app configuration.
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
@Configuration
@Import({ServiceConfiguration.class, DaoEsConfiguration.class, CacheConfiguration.class, RestConfiguration.class})
@PropertySources({ @PropertySource("file:${app.home}/hair-app.properties") })
@ComponentScan({ "com.hair.business.app.main" })
public class HairConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();

        return propertySourcesPlaceholderConfigurer;
    }
}
