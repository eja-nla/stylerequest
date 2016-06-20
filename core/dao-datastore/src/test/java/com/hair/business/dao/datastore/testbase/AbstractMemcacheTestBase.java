package com.hair.business.dao.datastore.testbase;

import static com.google.appengine.api.memcache.MemcacheServiceFactory.getMemcacheService;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Olukorede Aguda on 08/06/2016.
 */
public class AbstractMemcacheTestBase {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    // Run this test twice to prove we're not leaking any state across tests.
    private void doTest() {
        MemcacheService ms = getMemcacheService();
        assertFalse(ms.contains("yar"));
        ms.put("yar", "foo");
        assertTrue(ms.contains("yar"));
    }

    @Test
    public void testInsert1() {
        doTest();
    }

    @Test
    public void testInsert2() {
        doTest();
    }
}
