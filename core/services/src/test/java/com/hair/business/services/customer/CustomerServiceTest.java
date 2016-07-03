package com.hair.business.services.customer;

import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createLocation;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.dao.datastore.abstractRepository.Repository;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Olukorede Aguda on 23/06/2016.
 */
public class CustomerServiceTest extends AbstractServicesTestBase {

    CustomerService cs;

    @Before
    public void setUp(){
        cs = injector.getInstance(CustomerService.class);
    }

    @Test
    public void testPlaceStyleRequest() throws Exception {
        Merchant m = createMerchant();
        injector.getInstance(Repository.class).saveOne(m);

        cs.placeStyleRequest(createStyle(), createCustomer(), m, createLocation(), DateTime.now());
    }

    @Test
    public void testDeactivateCustomer() {
        Customer customer = createCustomer();
        cs.deactivateCustomer(customer);
        assertThat(customer.isActive(), is(false));
    }
}