package com.hair.business.dao.datastore.ofy;

import com.googlecode.objectify.impl.LoaderImpl;

/**
 * Ofy loader.
 *
 * Extend the Loader command with our own logic
 *
 * Created by Olukorede Aguda on 22/06/2016.
 */
public class OfyLoader extends LoaderImpl<OfyLoader> {


    public OfyLoader(Ofy base) {
        super(base);
    }

}