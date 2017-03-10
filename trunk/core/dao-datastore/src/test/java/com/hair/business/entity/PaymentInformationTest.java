package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createPaymentInfo;

import com.hair.business.beans.entity.PaymentInformation;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Logger;

/**
 *
 * Created by olukoredeaguda on 09/03/2017.
 */
public class PaymentInformationTest extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(CustomerTest.class.getName());

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {

        PaymentInformation paymentInformation = createPaymentInfo(4L);

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(paymentInformation));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/paymentInformation.json")));
        PaymentInformation paymentInformation2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, PaymentInformation.class);

        TEST_UTILS.validateFieldsAreEqual(paymentInformation, paymentInformation2);
    }



}