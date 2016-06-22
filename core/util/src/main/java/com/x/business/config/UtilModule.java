package com.x.business.config;

import com.google.inject.AbstractModule;

import com.x.business.notif.email.EmailSender;
import com.x.business.notif.email.EmailSenderImpl;


/**
 * Created by Olukorede Aguda on 21/06/2016.
 */
public class UtilModule extends AbstractModule {


    @Override
    protected void configure() {

        bind(EmailSender.class).to(EmailSenderImpl.class);
    }

}
