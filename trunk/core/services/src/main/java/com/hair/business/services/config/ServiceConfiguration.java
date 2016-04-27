package com.hair.business.services.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Main services config
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
@Configuration
@Import({ ClientConfiguration.class})
@ComponentScan({ "com.hair.business.services" })
public class ServiceConfiguration {

}
