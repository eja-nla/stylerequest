package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createLocation;

import com.hair.business.beans.entity.Location;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Olukorede Aguda on 20/08/2016.
 */
public class LocationTest  extends AbstractDatastoreTestBase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {

        Location location1 = createLocation();

        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/location.json")));
        Location location2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Location.class);

        TEST_UTILS.validateFieldsAreEqual(location1, location2);
    }



}