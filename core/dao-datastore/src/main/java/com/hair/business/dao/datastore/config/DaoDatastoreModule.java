package com.hair.business.dao.datastore.config;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.ofy.OfyService;
import com.hair.business.dao.datastore.repository.ObjectifyDatastoreRepositoryImpl;
import com.hair.business.dao.datastore.stereotype.DatastoreTransaction;
import com.hair.business.dao.datastore.repository.DatastoreTransactInterceptor;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 *
 *
 */
public class DaoDatastoreModule extends AbstractModule {

    protected void configure() {

        requestStaticInjection(OfyService.class);

        bind(Repository.class).to(ObjectifyDatastoreRepositoryImpl.class);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(DatastoreTransaction.class), new DatastoreTransactInterceptor());

        requireBinding(Repository.class);
    }
}
