package com.hair.business.dao.datastore.ofy;


import com.googlecode.objectify.ObjectifyService;

import javax.inject.Inject;

/**
 * Gives us our custom version rather than the standard Objectify one.  Also responsible for setting up the static
 * OfyFactory instead of the standard ObjectifyFactory - registered in Guice
 *
 * Created by Olukorede Aguda on 22/06/2016.
 */
public class OfyService extends ObjectifyService {

    @Inject
    public static void setObjectifyFactory(OfyFactory factory) {
        ObjectifyService.setFactory(factory);
    }

    /**
     * @return our extension to Objectify
     */
    public static Ofy ofy() {
        return (Ofy)ObjectifyService.ofy();
    }

    /**
     * @return our extension to ObjectifyFactory
     */
    public static OfyFactory factory() {
        return (OfyFactory)ObjectifyService.factory();
    }

    public static void register(Class<?> clazz) {
        factory().register(clazz);
    }

}
