package com.hair.business.dao.datastore.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;

import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.impl.HairstyleElasticsearchRepositoryExt;
import com.hair.business.dao.datastore.ofy.OfyService;
import com.hair.business.dao.datastore.repository.DatastoreTransactInterceptor;
import com.hair.business.dao.datastore.repository.ObjectifyDatastoreRepositoryImpl;
import com.hair.business.dao.datastore.stereotype.DatastoreTransaction;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;

import javax.inject.Singleton;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 *
 *
 */
public class DaoDatastoreModule extends AbstractModule {

    @Override
    protected void configure() {

        requestStaticInjection(OfyService.class);

        bind(Repository.class).to(ObjectifyDatastoreRepositoryImpl.class).in(Singleton.class);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(DatastoreTransaction.class), new DatastoreTransactInterceptor());

        bind(HairstyleElasticsearchRepositoryExt.class).in(Singleton.class);

        requireBinding(Repository.class);

        install(this);
    }

    @Singleton
    @Provides
    private RestClient elasticClientProvider(){
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(System.getProperty("elastic.access.key"), System.getProperty("elastic.access.secret")));

        return RestClient.builder(new HttpHost(System.getProperty("elastic.url"), Integer.valueOf(System.getProperty("elastic.port")), "https"))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(10000))
                .build();
    }
}
