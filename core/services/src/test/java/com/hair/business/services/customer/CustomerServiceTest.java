package com.hair.business.services.customer;

import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.pushNotification.SendPushNotificationToApnsTask;
import com.x.business.notif.Notification;
import com.x.business.scheduler.TaskQueue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by Olukorede Aguda on 23/06/2016.
 */
public class CustomerServiceTest extends AbstractServicesTestBase {

    CustomerService cs;
    Repository repository;
    TaskQueue queue1 = Mockito.mock(TaskQueue.class);
    TaskQueue queue2 = Mockito.mock(TaskQueue.class);


    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        cs = new CustomerServiceImpl(repository, queue1, queue2);
    }

    @Test
    public void testPlaceStyleRequest() throws Exception {
        Style style = createStyle();
        Customer customer = createCustomer();
        Merchant m = createMerchant();
        repository.saveFew(style, customer, m);

        StyleRequest styleRequest = cs.placeStyleRequest(style.getId(), customer.getId(), m.getId(), DateTime.now());

        verify(queue1, times(1)).add(any(Notification.class));
        verify(queue2, times(1)).add(any(SendPushNotificationToApnsTask.class));

        assertThat(styleRequest, is(notNullValue()));
        assertThat(styleRequest.getStyle().getRequestCount(), is(new Long(1)));

    }

    @Test
    public void testDeactivateCustomer() {
        Customer customer = createCustomer();
        cs.deactivateCustomer(customer);
        assertThat(customer.isActive(), is(false));
    }

    @Test
    public void testDate(){
        DateTime dt = new DateTime(1472499011389L, DateTimeZone.UTC);
        System.out.println(DateTime.now());
    }
}