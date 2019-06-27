package com.hair.business.services.customer;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import com.hair.business.dao.datastore.config.DaoDatastoreModule;
import com.hair.business.dao.datastore.ofy.OfyService;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Abstract services test base
 *
 * Created by Olukorede Aguda on 23/06/2016.
 */
public abstract class AbstractServicesTestBase {

    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    private static Closeable session;
    protected static Injector injector;

    @BeforeClass
    public static void setUpBeforeClass() {

        System.setProperty("elastic.access.key", "boo");
        System.setProperty("elastic.access.secret", "pass");
        System.setProperty("elastic.url", "url");
        System.setProperty("elastic.port", "123");

        injector = Guice.createInjector(new DaoDatastoreModule());

        session = OfyService.begin();
        helper.setUp();
    }

    @AfterClass
    public static void tearDown() {
        AsyncCacheFilter.complete();
        session.close();
        helper.tearDown();
    }
}