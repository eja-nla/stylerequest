package com.hair.business.dao.datastore.config;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.ofy.OfyService;
import com.hair.business.dao.datastore.repository.ObjectifyDatastoreRepositoryImpl;
import com.hair.business.dao.datastore.stereotype.Transact;
import com.hair.business.dao.datastore.repository.TransactInterceptor;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 *
 *
 */
public class DaoDatastoreModule extends AbstractModule {

    protected void configure() {

        requestStaticInjection(OfyService.class);

        bind(Repository.class).to(ObjectifyDatastoreRepositoryImpl.class);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transact.class), new TransactInterceptor());

        requireBinding(Repository.class);
    }
}