package com.hair.business.services.config;

import com.google.inject.AbstractModule;

import com.hair.business.services.customer.CustomerService;
import com.hair.business.services.customer.CustomerServiceImpl;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 */
public class ServicesModule extends AbstractModule {

    protected void configure() {

        bind(CustomerService.class).to(CustomerServiceImpl.class);
    }
}
