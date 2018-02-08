package com.hair.business.services.customer;

import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static com.x.y.EntityTestConstants.createStyleRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.StyleRequestService;
import com.hair.business.services.merchant.MerchantService;
import com.hair.business.services.merchant.MerchantServiceImpl;
import com.x.business.scheduler.TaskQueue;

import org.joda.time.DateTime;
import org.junit.Before;
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

    @Test // figure out how to find time overlap
    public void testIsNotBooked() {
        StyleRequest styleRequest = createSR(StyleRequestState.ACCEPTED);

        System.out.println(repository.findOne(styleRequest.getId(), StyleRequest.class).getId());
        assertThat(merchantService.isBooked(styleRequest.getMerchant().getId(),
                styleRequest.getAppointmentStartTime(), styleRequest.getAppointmentStartTime().plusMinutes(styleRequest.getStyle().getDurationEstimate())),
                is(false));
    }

    @Test
    public void testIsBooked() {
        StyleRequest existing = createSR(StyleRequestState.ACCEPTED);

        System.out.println(repository.findOne(existing.getId(), StyleRequest.class).getPermanentId() + " " + existing.getAppointmentStartTime());

        DateTime start = existing.getAppointmentStartTime().minusMinutes(5);
        DateTime end = existing.getAppointmentStartTime().plusMinutes(existing.getStyle().getDurationEstimate());

        System.out.println(start + ":start   end:" + end);
        //assertThat(merchantService.isBooked(existing.getPermanentId(), start, end), is(true));
    }

    private StyleRequest createSR(StyleRequestState state){
        StyleRequest styleRequest = createStyleRequest();
        styleRequest.setState(state);
        Merchant m = createMerchant();
        Style s = createStyle();
        s.setDurationEstimate(59);
        styleRequest.setStyle(s);
        styleRequest.setMerchant(m);
        styleRequest.setAppointmentStartTime(DateTime.now().plusMinutes(2));
        repository.saveFew(m, s, styleRequest);

        return styleRequest;
    }
}