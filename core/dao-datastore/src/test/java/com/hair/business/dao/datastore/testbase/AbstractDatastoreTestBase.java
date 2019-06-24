package com.hair.business.dao.datastore.testbase;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;

import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import com.hair.business.dao.datastore.config.DaoDatastoreModule;
import com.hair.business.dao.datastore.ofy.OfyService;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * All tests should extend this class to set up the GAE environment.
 *
 * @See  https://github.com/sappenin/objectify-utils/blob/master/src/test/java/com/googlecode/objectify/test/util/TestBase.java
 * Created by Olukorede Aguda on 08/06/2016.
 */
public class AbstractDatastoreTestBase {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    protected Closeable session;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("elastic.access.key", "boo");
        System.setProperty("elastic.access.secret", "pass");
        System.setProperty("elastic.url", "url");
        System.setProperty("elastic.port", "123");
        Guice.createInjector(new DaoDatastoreModule());
    }

    @Before
    public void setUp() {
        this.session = OfyService.begin();
        helper.setUp();
    }

    @After
    public void tearDown() {

        AsyncCacheFilter.complete();

        this.session.close();
        helper.tearDown();
    }

    // Run this test twice to prove we're not leaking any state across tests.
    private void doTest() {
        assertEquals(0, ds().prepare(new Query("yam")).countEntities(withLimit(10)));
        ds().put(new Entity("yam"));
        ds().put(new Entity("yam"));
        assertEquals(2, ds().prepare(new Query("yam")).countEntities(withLimit(10)));
    }

    @Test
    public void datastoreLeakageTest0() {
        doTest();
    }

    @Test
    public void datastoreLeakageTest1() {
        doTest();
    }

    /**
     * Get a DatastoreService
     */
    protected DatastoreService ds() {

        return DatastoreServiceFactory.getDatastoreService();
    }
}
