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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

/**
 * Created by Olukorede Aguda on 23/06/2016.
 */
public class CustomerServiceTest extends AbstractServicesTestBase {

    CustomerService cs;
    Repository repository;
    TaskQueue queue1 = Mockito.mock(TaskQueue.class);
    TaskQueue apnsQueue = Mockito.mock(TaskQueue.class);


    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        cs = new CustomerServiceImpl(repository, queue1, apnsQueue);

        try {
            System.setProperty("SENDGRID_NEW_STYLE_EMAIL_TEMPLATE_FILE", new File("src/test/resources/newStyleTemplate.json").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPlaceStyleRequest() throws Exception {
        Style style = createStyle();
        Customer customer = createCustomer();
        Merchant m = createMerchant();
        repository.saveFew(style, customer, m);

        StyleRequest styleRequest = cs.placeStyleRequest(style.getId(), customer.getId(), m.getId(), DateTime.now());

        verify(queue1, times(1)).add(any(Notification.class));

        assertThat(styleRequest, is(notNullValue()));
        assertThat(styleRequest.getStyle().getRequestCount(), is(1L));

    }

    @Ignore("until apns feature activation")
    @Test
    public void testPlaceStyleRequestWithAPNS() throws Exception {
        Style style = createStyle();
        Customer customer = createCustomer();
        Merchant m = createMerchant();
        repository.saveFew(style, customer, m);

        cs.placeStyleRequest(style.getId(), customer.getId(), m.getId(), DateTime.now());

        verify(apnsQueue, times(1)).add(any(SendPushNotificationToApnsTask.class));

    }

    @Test
    public void testDeactivateCustomer() {
        Customer customer = createCustomer();
        cs.deactivateCustomer(customer);
        assertThat(customer.isActive(), is(false));
    }

}