package com.hair.business.services;

import static com.hair.business.beans.constants.StyleRequestState.ACCEPTED;
import static com.hair.business.beans.constants.StyleRequestState.CANCELLED;
import static com.hair.business.beans.constants.StyleRequestState.COMPLETED;
import static com.hair.business.beans.constants.StyleRequestState.PENDING;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.stereotype.Timed;
import com.x.business.utilities.Assert;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Appointment finder
 *
 * Convenience class for routine queries on stylerequests
 * A merchant can only have accepted, cancelled and completed requests
 * A customer can only have placed, cancelled, and completed requests
 *
 * Created by olukoredeaguda on 29/03/2017.
 */
public class AppointmentFinderExt implements AppointmentFinder {

    private final Repository repository;

    private static final List<String> MERCHANT_APPOINTMENTS_ACCEPTED_QUERY = Arrays.asList("merchantPermanentId ==", "state ==", "acceptedTime >=", "acceptedTime <=");
    private static final List<String> MERCHANT_APPOINTMENTS_CANCELLED_QUERY = Arrays.asList("merchantPermanentId ==", "state ==", "cancelledTime >=", "cancelledTime <=");
    private static final List<String> MERCHANT_APPOINTMENTS_COMPLETED_QUERY = Arrays.asList("merchantPermanentId ==", "state ==", "completedTime >=", "completedTime <=");

    private static final List<String> CUSTOMER_APPOINTMENTS_PLACED_QUERY = Arrays.asList("customerPermanentId ==", "state ==", "placedTime >=", "placedTime <=");
    private static final List<String> CUSTOMER_APPOINTMENTS_CANCELLED_QUERY = Arrays.asList("customerPermanentId ==", "state ==", "cancelledTime >=", "cancelledTime <=");
    private static final List<String> CUSTOMER_APPOINTMENTS_COMPLETED_QUERY = Arrays.asList("customerPermanentId ==", "state ==", "completedTime >=", "completedTime <=");
    @Inject
    public AppointmentFinderExt(Repository repository) {
        this.repository = repository;
    }


    @Timed
    @Override
    public List<StyleRequest> findMerchantAcceptedAppointments(Long merchantId, DateTime lower, DateTime upper) {
        List<Object> values = validate(merchantId, ACCEPTED, lower, upper);

        return repository.findByQuery(StyleRequest.class, MERCHANT_APPOINTMENTS_ACCEPTED_QUERY, values);
    }

    @Timed
    @Override
    public List<StyleRequest> findMerchantCancelledAppointments(Long merchantId, DateTime lower, DateTime upper) {
        List<Object> values = validate(merchantId, CANCELLED, lower, upper);

        return repository.findByQuery(StyleRequest.class, MERCHANT_APPOINTMENTS_CANCELLED_QUERY, values);
    }

    @Timed
    @Override
    public List<StyleRequest> findMerchantCompletedAppointments(Long merchantId, DateTime lower, DateTime upper) {
        List<Object> values = validate(merchantId, COMPLETED, lower, upper);

        return repository.findByQuery(StyleRequest.class, MERCHANT_APPOINTMENTS_COMPLETED_QUERY, values);
    }

    @Timed
    @Override
    public List<StyleRequest> findCustomerCancelledAppointments(Long customerId, DateTime lower, DateTime upper) {
        List<Object> values = validate(customerId, CANCELLED, lower, upper);

        return repository.findByQuery(StyleRequest.class, CUSTOMER_APPOINTMENTS_CANCELLED_QUERY, values);
    }

    @Timed
    @Override
    public List<StyleRequest> findCustomerPendingAppointments(Long customerId, DateTime lower, DateTime upper) {
        List<Object> values = validate(customerId, PENDING, lower, upper);

        return repository.findByQuery(StyleRequest.class, CUSTOMER_APPOINTMENTS_PLACED_QUERY, values);
    }


    @Timed
    @Override
    public List<StyleRequest> findCustomerCompletedAppointments(Long customerId, DateTime lower, DateTime upper) {
        List<Object> values = validate(customerId, COMPLETED, lower, upper);

        return repository.findByQuery(StyleRequest.class, CUSTOMER_APPOINTMENTS_COMPLETED_QUERY, values);
    }

    private List<Object> validate(Long id, StyleRequestState state, DateTime start, DateTime stop) {
        Assert.validId(id);

        return Arrays.asList(id, state, start, stop);
    }
}
