package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createLocation;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.repository.ObjectifyDatastoreRepositoryImpl;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 20/08/2016.
 *
 *
 */
public class StyleRequestTest extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(StyleRequestTest.class.getName());

    private Repository repo = new ObjectifyDatastoreRepositoryImpl();

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {
        StyleRequest styleRequest1 = createStyleRequest();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(styleRequest1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/stylerequest.json")));
        StyleRequest styleRequest2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, StyleRequest.class);

        TEST_UTILS.validateFieldsAreEqual(styleRequest1, styleRequest2);
    }

    private StyleRequest createStyleRequest(){
        Style style = createStyle(); // we need to save these guys first so that objectify can successfully create the live Ref<?>
        Merchant merchant = createMerchant();
        Customer customer = createCustomer();
        Location location = createLocation();
        repo.saveFew(style, merchant, customer, location);

        StyleRequest s = new StyleRequest(style, merchant, customer, location, StyleRequestState.ACCEPTED, DateTime.now(), DateTime.now().plusMinutes(style.getDurationEstimate()));
        s.setId(new Random().nextLong());
        s.setPermanentId(new Random().nextLong());
        return s;
    }

}