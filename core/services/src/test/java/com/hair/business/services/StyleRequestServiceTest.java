package com.hair.business.services;

import static com.x.y.EntityTestConstants.createAddOns;
import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static com.x.y.EntityTestConstants.createTransactionResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.joda.time.DateTime.now;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.TransactionResult;
import com.hair.business.beans.helper.PaymentOperation;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.customer.AbstractServicesTestBase;
import com.hair.business.services.merchant.MerchantService;
import com.hair.business.services.payment.stripe.StripePaymentService;
import com.hair.business.services.pushNotification.PushNotificationServiceInternal;
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
    private StripePaymentService stripe = Mockito.mock(StripePaymentService.class);
    private MerchantService merchantService = Mockito.mock(MerchantService.class);
    private PushNotificationServiceInternal pushNotification = Mockito.mock(PushNotificationServiceInternal.class);
    private StylerequestStateMgr stateMgr;

    public StyleRequestServiceTest() {
        repository = injector.getInstance(Repository.class);
        stateMgr = new StylerequestStateMgrImpl(repository);
        srs = new StyleRequestServiceImpl(repository, emailQueue, stripe, merchantService, stateMgr, pushNotification);

        when(stripe.authorize(any(StyleRequest.class), anyString())).thenReturn(createTransactionResult());
    }

    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        srs = new StyleRequestServiceImpl(repository, emailQueue, stripe, merchantService, stateMgr, pushNotification);

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
    public void testFindStyleRequest() {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);

        assertThat(srs.findStyleRequest(sr.getId()), is(notNullValue()));
    }

    @Test
    public void testPlaceStyleRequest() {
        StyleRequest styleRequest = placeStyleRequest();

        verify(emailQueue, times(1)).add(any(AbstractEmailNotification.class));

        assertThat(styleRequest, is(notNullValue()));
        assertThat(styleRequest.getStyle().getRequestCount(), is(1L));
    }

    @Ignore("until apns feature activation")
    @Test
    public void testPlaceStyleRequestWithAPNS() {
        placeStyleRequest();

        verify(apnsQueue, times(1)).add(any(SendPushNotificationToApnsTask.class));

    }

    @Test
    public void testAcceptStyleRequest() {
        StyleRequest sr = initStyleRequest(StyleRequestState.PENDING);
        TransactionResult srp = new TransactionResult();
        srp.setOperation(PaymentOperation.AUTHORIZE);
        sr.getTransactionResults().add(srp);
        srs.acceptStyleRequest(sr.getId());
        assertThat(sr.getState(), is(StyleRequestState.ACCEPTED));
    }

    @Test
    public void testCancelStyleRequest() {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);
        TransactionResult srp = new TransactionResult();
        srp.setOperation(PaymentOperation.AUTHORIZE);
        sr.getTransactionResults().add(srp);
        srs.cancelStyleRequest(sr.getId());

        StyleRequest updatedSr = srs.findStyleRequest(sr.getId());

        assertThat(updatedSr.getState(), is(StyleRequestState.CANCELLED));
    }

    @Test
    public void testCompleteStyleRequest() {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);
        TransactionResult srp = new TransactionResult();
        srp.setOperation(PaymentOperation.AUTHORIZE);
        sr.getTransactionResults().add(srp);
        srs.completeStyleRequest(sr.getId());
        StyleRequest updatedSr = srs.findStyleRequest(sr.getId());
        assertThat(updatedSr.getState(), is(StyleRequestState.COMPLETED));
    }

    @Test
    public void testUpdate() {
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

    private StyleRequest placeStyleRequest(){
        Style style = createStyle();
        Customer customer = createCustomer();
        Merchant m = createMerchant();
        repository.saveFew(style, customer, m);

        return srs.placeStyleRequest(createAddOns(), style.getId(), customer.getId(), m.getId(), now().plusHours(3));
    }
}