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
 * Created by Olukorede Aguda on 23/06/2016.
 */
public class AbstractServicesTestBase {

    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    protected static Closeable session;
    public static Injector injector;

    @BeforeClass
    public static void setUpBeforeClass() {

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