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
import com.hair.business.services.metrics.ExecTimeLoggerInterceptor;
import com.hair.business.services.payment.PaymentProcessor;
import com.hair.business.services.payment.paypal.PaypalPaymentProcessorImpl;
import com.hair.business.services.stereotype.Timed;
import com.paypal.base.rest.APIContext;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 *
 * Services module
 */
public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(CustomerService.class).to(CustomerServiceImpl.class);
        bind(MerchantService.class).to(MerchantServiceImpl.class);
        bind(StyleService.class).to(StyleServiceImpl.class);
        bind(StyleRequestService.class).to(StyleRequestServiceImpl.class);
        bind(PaymentProcessor.class).to(PaypalPaymentProcessorImpl.class);

        bind(APIContext.class).toInstance(new APIContext(System.getProperty("paypal.client.id"), System.getProperty("paypal.client.secret"), System.getProperty("paypal.environment")));

        bindInterceptor(any(), annotatedWith(Timed.class), new ExecTimeLoggerInterceptor());

    }
}
