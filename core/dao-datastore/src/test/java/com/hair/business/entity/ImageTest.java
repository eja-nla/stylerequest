package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createImage;

import com.hair.business.beans.entity.Image;
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
public class ImageTest  extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(ImageTest.class.getName());

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {

        Image image1 = createImage();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(image1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/image.json")));
        Image image2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Image.class);

        TEST_UTILS.validateFieldsAreEqual(image1, image2);
    }



}