package com.hair.business.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.StyleRequest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * Created by olukoredeaguda on 29/03/2017.
 */
@Ignore("fix issue with  file  path. These tests are already covered in StyleRequestServicetest anyway")
public class AppointmentFinderTest extends StyleRequestServiceTest {

    private static final DateTime baseDateTime = new DateTime();

    private AppointmentFinder appointmentFinder;

    public AppointmentFinderTest() {
    }

    @Override
    @Before
    public void setUp(){
        appointmentFinder = new AppointmentFinderExt(repository);
    }
    
    @Test
    public void testFindMerchantUpcomingAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);

        assertThat(appointmentFinder.findMerchantAcceptedAppointments(sr.getMerchantPermanentId(), baseDateTime.plusHours(2), baseDateTime.plusHours(5)).size(), is(1));
    }

    @Test
    public void testFindMerchantCancelledAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.CANCELLED);

        assertThat(appointmentFinder.findMerchantCancelledAppointments(sr.getMerchantPermanentId(), baseDateTime.plusHours(2), baseDateTime.plusHours(5)).size(), is(1));

    }

    @Test
    public void testFindMerchantPendingAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.PENDING);

        assertThat(appointmentFinder.findMerchantPendingAppointments(sr.getMerchantPermanentId(), baseDateTime.minusMinutes(1), baseDateTime.plusMinutes(1)).size(), is(1));

    }

    @Test
    public void testFindMerchantCompletedAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.COMPLETED);

        assertThat(appointmentFinder.findMerchantCompletedAppointments(sr.getMerchantPermanentId(), baseDateTime.plusHours(2), baseDateTime.plusHours(5)).size(), is(1));

    }

    @Test
    public void testFindCustomerCompletedAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.COMPLETED);

        assertThat(appointmentFinder.findCustomerCompletedAppointments(sr.getCustomerPermanentId(), baseDateTime.plusHours(2), baseDateTime.plusHours(5)).size(), is(1));

    }
    @Test
    public void testFindCustomerPendingAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.PENDING);

        assertThat(appointmentFinder.findCustomerPendingAppointments(sr.getCustomerPermanentId(), baseDateTime.plusHours(2), baseDateTime.plusHours(5)).size(), is(1));

    }
    @Test
    public void testFindCustomerCancelledAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.CANCELLED);

        assertThat(appointmentFinder.findCustomerCancelledAppointments(sr.getCustomerPermanentId(), baseDateTime.plusHours(2), baseDateTime.plusHours(5)).size(), is(1));

    }
    @Test
    public void testFindCustomerUpcomingAppointments() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);

        assertThat(appointmentFinder.findCustomerAcceptedAppointments(sr.getCustomerPermanentId(), baseDateTime.plusHours(2), baseDateTime.plusHours(5)).size(), is(1));

    }

    @Test
    public void testFindCustomerUpcomingAppointmentsUpperLower() throws Exception {
        StyleRequest sr = initStyleRequest(StyleRequestState.ACCEPTED);

        assertThat(appointmentFinder.findCustomerAcceptedAppointments(sr.getCustomerPermanentId(), baseDateTime.plusHours(2), baseDateTime.plusHours(4)).size(), is(1));
    }

}