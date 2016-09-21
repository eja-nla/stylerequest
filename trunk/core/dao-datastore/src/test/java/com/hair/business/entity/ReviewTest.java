package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createReview;

import com.hair.business.beans.entity.Review;
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
public class ReviewTest  extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(ReviewTest.class.getName());

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {
        Review review1 = createReview();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(review1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/review.json")));
        Review review2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Review.class);

        TEST_UTILS.validateFieldsAreEqual(review1, review2);
    }



}