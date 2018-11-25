package com.hair.business.services;

import com.hair.business.beans.entity.StyleRequest;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * Appointment finder
 *
 * Created by olukoredeaguda on 29/03/2017.
 */
public interface AppointmentFinder {

    /**
     * Returns merchant's accepted appointments (whose start time is) between two given dateTime bounds
     */
    Collection<StyleRequest> findMerchantAcceptedAppointments(Long merchantId, DateTime lower, DateTime upper);

    /**
     * Returns merchant's cancelled appointments between two given dateTime bounds
     */
    Collection<StyleRequest> findMerchantCancelledAppointments(Long merchantId, DateTime lower, DateTime upper);

//    /**
//     * Returns merchant's pending appointments between two given dateTime bounds
//     */
//    Collection<StyleRequest> findMerchantPendingAppointments(Long merchantId, DateTime lower, DateTime upper);

    /**
     * Returns merchant's completed appointments between two given dateTime bounds
     */
    Collection<StyleRequest> findMerchantCompletedAppointments(Long merchantId, DateTime lower, DateTime upper);

//    /**
//     * Returns customer's upcoming appointments between two given dateTime bounds
//     */
//    Collection<StyleRequest> findCustomerAcceptedAppointments(Long customerId, DateTime lower, DateTime upper);

    /**
     * Returns customer's cancelled appointments between two given dateTime bounds
     */
    Collection<StyleRequest> findCustomerCancelledAppointments(Long customerId, DateTime lower, DateTime upper);

    /**
     * Returns customer's pending appointments between two given dateTime bounds
     */
    Collection<StyleRequest> findCustomerPendingAppointments(Long customerId, DateTime lower, DateTime upper);

    /**
     * Returns customer's completed appointments between two given dateTime bounds
     */
    Collection<StyleRequest> findCustomerCompletedAppointments(Long customerId, DateTime lower, DateTime upper);

}
