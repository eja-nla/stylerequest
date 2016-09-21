package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createDevice;

import com.hair.business.beans.entity.Device;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 20/08/2016.
 */
public class DeviceTest extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(DeviceTest.class.getName());

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {
        Device device1 = createDevice();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(device1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/device.json")));
        Device device2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Device.class);

        TEST_UTILS.validateFieldsAreEqual(device1, device2);
    }

}