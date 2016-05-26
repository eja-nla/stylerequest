package com.hair.business.dao.datastore.config;

import com.google.inject.AbstractModule;

import com.hair.business.dao.datastore.abstractRepository.AbstractAsyncRepository;
import com.hair.business.dao.datastore.abstractRepository.AbstractSyncRepository;
import com.hair.business.dao.datastore.repository.AsyncRepository;
import com.hair.business.dao.datastore.repository.ServiceAsyncRepository;
import com.hair.business.dao.datastore.repository.ServiceSyncRepository;
import com.hair.business.dao.datastore.repository.SyncRepository;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 *
 *
 */
public class DaoDatastoreModule extends AbstractModule {

    protected void configure() {
        bind(AbstractSyncRepository.class).to(SyncRepository.class);
        bind(SyncRepository.class).to(ServiceSyncRepository.class);

        bind(AbstractAsyncRepository.class).to(AsyncRepository.class);
        bind(AsyncRepository.class).to(ServiceAsyncRepository.class);

        requireBinding(ServiceSyncRepository.class);
        requireBinding(ServiceAsyncRepository.class);
    }
}
