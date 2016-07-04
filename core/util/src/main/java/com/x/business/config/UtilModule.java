package com.x.business.config;

import com.google.inject.AbstractModule;

import com.x.business.tasks.SendgridEmailHandler;

import javax.inject.Singleton;


/**
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class UtilModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(SendgridEmailHandler.class).in(Singleton.class);
    }

}
