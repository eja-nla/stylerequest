package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createCustomer;

import com.hair.business.beans.entity.Customer;
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
public class CustomerTest extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(CustomerTest.class.getName());

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {

        Customer customer1 = createCustomer();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(customer1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/customer.json")));
        Customer customer2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Customer.class);

        TEST_UTILS.validateFieldsAreEqual(customer1, customer2);
    }
}