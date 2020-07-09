package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;

import com.hair.business.beans.entity.TransactionResult;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;
import com.x.y.EntityTestConstants;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Logger;

/**
 * Created by ejanla on 7/8/20.
 */
public class TransactionResultTest extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(CustomerTest.class.getName());

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {

        TransactionResult tr1 = EntityTestConstants.createTransactionResult();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tr1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/transactionResult.json")));
        TransactionResult tr2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, TransactionResult.class);

        TEST_UTILS.validateFieldsAreEqual(tr1, tr2);
    }
}