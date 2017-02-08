package com.hair.business.services.customer;

import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyleRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.StyleRequestService;
import com.hair.business.services.merchant.MerchantService;
import com.hair.business.services.merchant.MerchantServiceImpl;
import com.x.business.scheduler.TaskQueue;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by Olukorede Aguda on 29/08/2016.
 *
 *
 */
public class MerchantServiceTest extends AbstractServicesTestBase {
    private MerchantService merchantService;
    private Repository repository;
    private StyleRequestService styleRequestService = Mockito.mock(StyleRequestService.class);
    private TaskQueue emailQueue = Mockito.mock(TaskQueue.class);
    private TaskQueue apnsQueue = Mockito.mock(TaskQueue.class);


    @Before
    public void setUp() {
        repository = injector.getInstance(Repository.class);
        merchantService = new MerchantServiceImpl(repository, styleRequestService, emailQueue, apnsQueue);
    }

    @Test @Ignore // figure out how to find time overlap
    public void testIsBooked() {
        StyleRequest styleRequest = createStyleRequest();
        styleRequest.setState(StyleRequestState.ACCEPTED);
        Merchant m = createMerchant();
        styleRequest.setMerchant(m);
        styleRequest.setAppointmentStartTime(DateTime.now());
        repository.saveFew(styleRequest, m);

        assertThat(merchantService.isBooked(styleRequest.getMerchant().getId(), DateTime.now()), is(true));
    }
}