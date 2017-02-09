package com.hair.business.services.customer;

import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createPayment;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Payment;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.business.scheduler.TaskQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olukorede Aguda on 23/06/2016.
 *
 *
 */
public class CustomerServiceTest extends AbstractServicesTestBase {

    private CustomerService cs;
    private Repository repository;
    private TaskQueue emailQueue = Mockito.mock(TaskQueue.class);
    private TaskQueue apnsQueue = Mockito.mock(TaskQueue.class);

    private Customer customer;

    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        cs = new CustomerServiceImpl(repository, emailQueue, apnsQueue);

        customer = createCustomer();
        repository.saveOne(customer);
        try {
            System.setProperty("SENDGRID_NEW_STYLE_EMAIL_TEMPLATE_FILE", new File("src/test/resources/styleTemplate_pending.json").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testDeactivateCustomer() {
        cs.deactivateCustomer(customer);
        assertThat(customer.isActive(), is(false));
    }

    @Test
    public void testRatings() {
        Map<Integer, Integer> testRatings = new HashMap<>();
        testRatings.put(0, 0); testRatings.put(1, 0);testRatings.put(2, 0);testRatings.put(3, 0);testRatings.put(4, 3);testRatings.put(5, 2);

        customer.setRatings(testRatings);

        cs.saveCustomer(customer);
        cs.updateRating(customer.getId(), 5);
        assertThat(customer.getScore(), is(4.5));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRatingsFail() {
        Map<Integer, Integer> testRatings = new HashMap<>();
        testRatings.put(0, 0); testRatings.put(1, 0);testRatings.put(2, 0);testRatings.put(3, 0);testRatings.put(4, 3);testRatings.put(5, 2);
        customer.setRatings(testRatings);
        cs.saveCustomer(customer);

        cs.updateRating(customer.getId(), 0);
    }

    @Test
    public void testUpdatePaymentInfo() {
        Payment payment = createPayment();
        cs.updatePaymentInfo(customer.getId(), payment);

        assertThat(customer.getPayment(), is(payment));
    }

}