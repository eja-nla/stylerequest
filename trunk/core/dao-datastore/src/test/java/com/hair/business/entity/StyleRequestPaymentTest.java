package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;

import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.dao.datastore.abstractRepository.ObjectifyRepository;
import com.hair.business.dao.datastore.repository.ObjectifyDatastoreRepositoryImpl;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
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
public class StyleRequestPaymentTest extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(StyleRequestPaymentTest.class.getName());

    private ObjectifyRepository repo = new ObjectifyDatastoreRepositoryImpl();

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {
        StyleRequestPayment pay1 = createPayment();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pay1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/payment.json")));
        StyleRequestPayment pay2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, StyleRequestPayment.class);

        TEST_UTILS.validateFieldsAreEqual(pay1, pay2);
    }

    private StyleRequestPayment createPayment(){
        Merchant merchant = createMerchant(); // we need to save these guys first so that objectify can successfully create the live Ref<?>
        Customer customer = createCustomer();
        repo.saveFew(merchant, customer);

        StyleRequestPayment p = new StyleRequestPayment(2432.545D, 435432L, 3254234L, true, PaymentType.PAYPAL);

        p.setId(new Random().nextLong());
        p.setPermanentId(new Random().nextLong());
        return p;
    }

}