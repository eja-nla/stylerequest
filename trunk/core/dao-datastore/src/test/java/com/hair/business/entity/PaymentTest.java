package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;

import com.hair.business.beans.constants.MerchantType;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Payment;
import com.hair.business.dao.datastore.abstractRepository.ObjectifyRepository;
import com.hair.business.dao.datastore.repository.ObjectifyDatastoreRepositoryImpl;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

/**
 * Created by Olukorede Aguda on 20/08/2016.
 *
 *
 */
public class PaymentTest extends AbstractDatastoreTestBase {

    ObjectifyRepository repo = new ObjectifyDatastoreRepositoryImpl();

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {
        Payment pay1 = createPayment();

        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/payment.json")));
        Payment pay2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, Payment.class);

        TEST_UTILS.validateFieldsAreEqual(pay1, pay2);
    }

    private Payment createPayment(){
        Merchant merchant = createMerchant(); // we need to save these guys first so that objectify can successfully create the live Ref<?>
        Customer customer = createCustomer();
        repo.saveFew(merchant, customer);

        Payment p = new Payment(new Random().nextLong(), customer, merchant, true, MerchantType.PAYPAL, DateTime.now());

        p.setId(new Random().nextLong());
        p.setPermanentId(new Random().nextLong());
        return p;
    }

}