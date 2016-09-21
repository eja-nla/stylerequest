
package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createAddress;

import com.hair.business.beans.entity.Address;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 20/08/2016.
 *
 *
 */
public class AddressTest extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(AddressTest.class.getName());

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {
        Address add1 = createAddress();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(add1));

        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/address.json")));
        Address add2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Address.class);

        TEST_UTILS.validateFieldsAreEqual(add1, add2);
    }

}