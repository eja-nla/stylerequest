package com.hair.business.services.customer;

import static com.x.y.EntityTestConstants.createCustomer;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.hair.business.beans.entity.Customer;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.business.scheduler.TaskQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

/**
 * Created by Olukorede Aguda on 23/06/2016.
 */
public class CustomerServiceTest extends AbstractServicesTestBase {

    private CustomerService cs;
    private Repository repository;
    private TaskQueue emailQueue = Mockito.mock(TaskQueue.class);
    private TaskQueue apnsQueue = Mockito.mock(TaskQueue.class);


    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        cs = new CustomerServiceImpl(repository, emailQueue, apnsQueue);

        try {
            System.setProperty("SENDGRID_NEW_STYLE_EMAIL_TEMPLATE_FILE", new File("src/test/resources/styleTemplate_pending.json").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testDeactivateCustomer() {
        Customer customer = createCustomer();
        cs.deactivateCustomer(customer);
        assertThat(customer.isActive(), is(false));
    }

}