package com.hair.business.services.config;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import com.google.inject.AbstractModule;

import com.hair.business.services.StyleRequestService;
import com.hair.business.services.StyleRequestServiceImpl;
import com.hair.business.services.StyleService;
import com.hair.business.services.StyleServiceImpl;
import com.hair.business.services.customer.CustomerService;
import com.hair.business.services.customer.CustomerServiceImpl;
import com.hair.business.services.merchant.MerchantService;
import com.hair.business.services.merchant.MerchantServiceImpl;
import com.hair.business.services.metrics.LoggingInterceptor;
import com.hair.business.services.stereotype.ExecutionTimer;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 */
public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(CustomerService.class).to(CustomerServiceImpl.class);
        bind(MerchantService.class).to(MerchantServiceImpl.class);
        bind(StyleService.class).to(StyleServiceImpl.class);
        bind(StyleRequestService.class).to(StyleRequestServiceImpl.class);

        bindInterceptor(any(), annotatedWith(ExecutionTimer.class), new LoggingInterceptor());
    }
}
