package com.hair.business.services;

import com.hair.business.beans.entity.StyleRequest;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * Style Service
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public interface StyleRequestService {

    StyleRequest findStyleRequest(Long id);

    /**
     * All Style requests in ACCEPTED state for this merchant
     */
    Collection<StyleRequest> findUpcomingAppointments(Long merchantId, DateTime timeAgo);
    Collection<StyleRequest> findCancelledAppointments(Long merchantId, DateTime timeAgo);
    Collection<StyleRequest> findPendingAppointments(Long merchantId, DateTime timeAgo);
    Collection<StyleRequest> findCompletedAppointments(Long merchantId, DateTime timeAgo);

    /**
     * Place a new style request for this customer
     * @param styleId the ID of the style to be made
     * @param customerId customer id
     * @param merchantId merchant id
     * @param appointmentTime when the customer will be styled
     *
     * Sets the location to the location of the merchant
     */
    StyleRequest placeStyleRequest(Long styleId, Long customerId, Long merchantId, DateTime appointmentTime);

    void updateStyleRequest(StyleRequest styleRequest);

    /**
     * Cancels a placed style request
     */
    void cancelStyleRequest(StyleRequest styleRequest);
}
