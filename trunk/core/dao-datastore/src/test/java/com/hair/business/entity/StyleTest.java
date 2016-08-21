package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createStyle;

import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Olukorede Aguda on 20/08/2016.
 */
public class StyleTest  extends AbstractDatastoreTestBase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {

        Style style1 = createStyle();

        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/style.json")));
        Style style2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Style.class);

        TEST_UTILS.validateFieldsAreEqual(style1, style2);
    }
}