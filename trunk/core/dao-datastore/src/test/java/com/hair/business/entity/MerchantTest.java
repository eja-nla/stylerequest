package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createMerchant;

import com.hair.business.beans.entity.Merchant;
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
public class MerchantTest  extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(MerchantTest.class.getName());

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {
        Merchant merchant1 = createMerchant();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(merchant1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/merchant.json")));
        Merchant merchant2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Merchant.class);

        TEST_UTILS.validateFieldsAreEqual(merchant1, merchant2);
    }

}