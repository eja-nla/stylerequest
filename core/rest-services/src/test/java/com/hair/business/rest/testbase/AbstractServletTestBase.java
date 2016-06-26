package com.hair.business.rest.testbase;

import static org.mockito.Mockito.mock;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hair.business.rest.RestServicesModule;
import com.hair.business.services.customer.CustomerService;

import org.junit.BeforeClass;

import javax.servlet.ServletContext;

/**
 * Created by Olukorede Aguda on 26/06/2016.
 */
public class AbstractServletTestBase {

    public static Injector injector;


    @BeforeClass
    public static void before(){
        RestServicesModule x = new RestServicesModule();
        x.setServletContext(mock(ServletContext.class));
        injector = Guice.createInjector(x, new AbstractModule() {
            @Override
            protected void configure() {
                // other mocks
                bind(CustomerService.class).toInstance(mock(CustomerService.class));
                bind(ObjectMapper.class).toInstance(mock(ObjectMapper.class));
            }
        });
    }

}
