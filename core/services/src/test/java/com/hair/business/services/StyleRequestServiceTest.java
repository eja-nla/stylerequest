package com.hair.business.services;

import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.customer.AbstractServicesTestBase;
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
 * Created by olukoredeaguda on 07/01/2017.
 */
public class StyleRequestServiceTest extends AbstractServicesTestBase {

    private Repository repository;
    private StyleRequestService srs;
    private TaskQueue emailQueue = Mockito.mock(TaskQueue.class);
    private TaskQueue apnsQueue = Mockito.mock(TaskQueue.class);


    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        srs = new StyleRequestServiceImpl(repository, emailQueue, apnsQueue);

        try {
            System.setProperty("SENDGRID_NEW_STYLE_EMAIL_TEMPLATE_FILE", new File("src/test/resources/newStyleTemplate.json").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testPlaceStyleRequest() throws Exception {
        StyleRequest styleRequest = placeStyleRequest();

        verify(emailQueue, times(1)).add(any(Notification.class));

        assertThat(styleRequest, is(notNullValue()));
        assertThat(styleRequest.getStyle().getRequestCount(), is(1L));
    }

    @Ignore("until apns feature activation")
    @Test
    public void testPlaceStyleRequestWithAPNS() throws Exception {
        placeStyleRequest();

        verify(apnsQueue, times(1)).add(any(SendPushNotificationToApnsTask.class));

    }

    @Test
    public void testFindUpcomingAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);

        assertThat(srs.findUpcomingAppointments(sr.getMerchantPermanentId(), new DateTime().minusDays(1)).size(), is(1));

    }

    @Test
    public void testFindCancelledAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.CANCELLED);

        assertThat(srs.findCancelledAppointments(sr.getMerchantPermanentId(), new DateTime().minusDays(1)).size(), is(1));

    }

    @Test
    public void testFindPendingAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.PENDING);

        assertThat(srs.findPendingAppointments(sr.getMerchantPermanentId(), new DateTime().minusDays(1)).size(), is(1));

    }

    @Test
    public void testFindCompletedAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.COMPLETED);

        assertThat(srs.findCompletedAppointments(sr.getMerchantPermanentId(), new DateTime().minusDays(1)).size(), is(1));

    }

    @Test
    public void testUpdate() throws Exception {
        StyleRequest sr = placeStyleRequest();
        sr.setState(StyleRequestState.IN_PROGRESS);
        srs.updateStyleRequest(sr);

        assertThat(sr.getState(), is(StyleRequestState.IN_PROGRESS));
    }

    private StyleRequest initStyleRequest(StyleRequestState state){
        StyleRequest sr = placeStyleRequest();

        sr.setState(state);
        repository.saveOne(sr);

        return sr;
    }

    private StyleRequest placeStyleRequest(){
        Style style = createStyle();
        Customer customer = createCustomer();
        Merchant m = createMerchant();
        repository.saveFew(style, customer, m);

        return srs.placeStyleRequest(style.getId(), customer.getId(), m.getId(), DateTime.now());
    }
}