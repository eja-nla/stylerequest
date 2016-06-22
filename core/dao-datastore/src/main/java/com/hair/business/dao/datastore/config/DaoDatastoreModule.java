package com.hair.business.dao.datastore.config;

import com.google.inject.AbstractModule;

import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.ofy.OfyService;
import com.hair.business.dao.datastore.repository.ObjectifyDatastoreRepositoryImpl;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 *
 *
 */
public class DaoDatastoreModule extends AbstractModule {

    protected void configure() {

        requestStaticInjection(OfyService.class);

        bind(Repository.class).to(ObjectifyDatastoreRepositoryImpl.class);

        requireBinding(Repository.class);

    }
}
