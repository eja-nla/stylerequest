package com.hair.business.dao.datastore.ofy;

import com.google.inject.Injector;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Payment;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Our version of ObjectifyFactory which integrates with Guice.
 *
 * See https://github.com/stickfigure/motomapia/blob/master/src/main/java/com/motomapia/OfyFactory.java
 *
 * Created by Olukorede Aguda on 22/06/2014
 */

@Singleton
public class OfyFactory extends ObjectifyFactory{

    private Injector injector;

    @Inject
    public OfyFactory(Injector injector) {
        this.injector = injector;

        JodaTimeTranslators.add(this);

        this.register(Customer.class);
        this.register(Merchant.class);
        this.register(Style.class);
        this.register(StyleRequest.class);
        this.register(Payment.class);

    }

    /** Use guice to make instances instead! */
    @Override
    public <T> T construct(Class<T> type) {
        return injector.getInstance(type);
    }

    @Override
    public Ofy begin() {
        return new Ofy(this);
    }
}