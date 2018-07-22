package com.hair.business.services;

import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.joda.time.DateTime.now;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.customer.AbstractServicesTestBase;
import com.hair.business.services.merchant.MerchantService;
import com.hair.business.services.payment.PaymentService;
import com.hair.business.services.pushNotification.SendPushNotificationToApnsTask;
import com.hair.business.services.state.StylerequestStateMgr;
import com.hair.business.services.state.StylerequestStateMgrImpl;
import com.x.business.notif.AbstractEmailNotification;
import com.x.business.scheduler.TaskQueue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

/**
 * Created by olukoredeaguda on 07/01/2017.
 *
 *
 */
public class StyleRequestServiceTest extends AbstractServicesTestBase {

    Repository repository;
    private StyleRequestService srs;
    private TaskQueue emailQueue = Mockito.mock(TaskQueue.class);
    private TaskQueue apnsQueue = Mockito.mock(TaskQueue.class);
    private PaymentService paymentService = Mockito.mock(PaymentService.class);
    private MerchantService merchantService = Mockito.mock(MerchantService.class);
    private StylerequestStateMgr stateMgr;

    public StyleRequestServiceTest() {
        repository = injector.getInstance(Repository.class);
        stateMgr = new StylerequestStateMgrImpl(repository);
        srs = new StyleRequestServiceImpl(repository, emailQueue, apnsQueue, paymentService, merchantService, stateMgr);
    }

    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        srs = new StyleRequestServiceImpl(repository, emailQueue, apnsQueue, paymentService, merchantService, stateMgr);

        // See sendgrid section in appengine-web.xml
        try {
            System.setProperty("sendgrid.placed.merchant.stylerequest.email.template", new File("src/test/resources/styleTemplate.customer.placed.json").getCanonicalPath());
            System.setProperty("sendgrid.cancelled.merchant.stylerequest.email.template", new File("src/test/resources/styleTemplate.customer.placed.json").getCanonicalPath());
            System.setProperty("sendgrid.accepted.merchant.stylerequest.email.template", new File("src/test/resources/styleTemplate.customer.placed.json").getCanonicalPath());
            System.setProperty("sendgrid.completed.merchant.stylerequest.email.template", new File("src/test/resources/styleTemplate.customer.placed.json").getCanonicalPath());
            System.setProperty("sendgrid.placed.customer.stylerequest.email.template", new File("src/test/resources/styleTemplate.customer.placed.json").getCanonicalPath());
            System.setProperty("sendgrid.cancelled.customer.stylerequest.email.template", new File("src/test/resources/styleTemplate.customer.placed.json").getCanonicalPath());
            System.setProperty("sendgrid.accepted.customer.stylerequest.email.template", new File("src/test/resources/styleTemplate.customer.placed.json").getCanonicalPath());
            System.setProperty("sendgrid.completed.customer.stylerequest.email.template", new File("src/test/resources/styleTemplate.customer.placed.json").getCanonicalPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindStyleRequest() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);

        assertThat(srs.findStyleRequest(sr.getId()), is(notNullValue()));
    }

    @Test
    public void testPlaceStyleRequest() throws Exception {
        StyleRequest styleRequest = placeStyleRequest();

        verify(emailQueue, times(1)).add(any(AbstractEmailNotification.class));

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
    public void testAcceptStyleRequest() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.PENDING);
        srs.acceptStyleRequest(sr.getId(), new Preferences());
        assertThat(sr.getState(), is(StyleRequestState.ACCEPTED));
    }

    @Test
    public void testCancelStyleRequest() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);
        srs.cancelStyleRequest(sr.getId(), new Preferences());

        StyleRequest updatedSr = srs.findStyleRequest(sr.getId());

        assertThat(updatedSr.getState(), is(StyleRequestState.CANCELLED));
    }

    @Test
    public void testCompleteStyleRequest() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);
        srs.completeStyleRequest(sr.getId(), new Preferences());
        StyleRequest updatedSr = srs.findStyleRequest(sr.getId());
        assertThat(updatedSr.getState(), is(StyleRequestState.COMPLETED));
    }

    @Test
    public void testUpdate() throws Exception {
        StyleRequest sr = placeStyleRequest();
        sr.setState(StyleRequestState.IN_PROGRESS);
        srs.updateStyleRequest(sr);

        assertThat(sr.getState(), is(StyleRequestState.IN_PROGRESS));
    }

    StyleRequest initStyleRequest(StyleRequestState state){
        StyleRequest sr = placeStyleRequest();

        sr.setState(state);
        repository.saveOne(sr);

        return sr;
    }

    StyleRequest placeStyleRequest(){
        Style style = createStyle();
        Customer customer = createCustomer();
        Merchant m = createMerchant();
        repository.saveFew(style, customer, m);

        return srs.placeStyleRequest(style.getId(), customer.getId(), m.getId(), now().plusHours(3));
    }
}