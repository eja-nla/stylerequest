package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createPayment;

import com.hair.business.beans.entity.Payment;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Olukorede Aguda on 20/08/2016.
 *
 *
 */
public class PaymentTest  extends AbstractDatastoreTestBase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Ignore // until i figure out how to test Ofy Ref. @see com.hair.business.beans.entity.Payment
    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {
        Payment pay1 = createPayment();
        System.out.println(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pay1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/payment.json")));
        Payment pay2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Payment.class);

        TEST_UTILS.validateFieldsAreEqual(pay1, pay2);
    }



}