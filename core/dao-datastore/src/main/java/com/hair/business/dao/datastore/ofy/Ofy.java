package com.hair.business.dao.datastore.ofy;



import com.googlecode.objectify.impl.ObjectifyImpl;

/**
 * Custom Ofy impl - uses custom OfyLoader and OfyFactory
 *
 * Created by Olukorede Aguda on 22/06/2016.
 */
public class Ofy extends ObjectifyImpl<Ofy> {

    public Ofy(OfyFactory base) {
        super(base);
    }

    @Override
    public OfyLoader load() {
        return new OfyLoader(this);
    }
}