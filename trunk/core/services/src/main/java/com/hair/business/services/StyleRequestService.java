package com.hair.business.services;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.StyleRequest;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * Style Service
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public interface StyleRequestService {

    /**
     * Finds stylerequest with given id
     */
    StyleRequest findStyleRequest(Long id);

    /**
     * Returns merchant's accepted appointments between now and an upper datetime limit
     */
    Collection<StyleRequest> findMerchantAcceptedAppointments(Long merchantId, DateTime limit);

    /**
     * Returns merchant's cancelled appointments between now and an upper datetime limit
     */
    Collection<StyleRequest> findMerchantCancelledAppointments(Long merchantId, DateTime limit);

    /**
     * Returns merchant's pending appointments between now and an upper datetime limit
     */
    Collection<StyleRequest> findMerchantPendingAppointments(Long merchantId, DateTime limit);

    /**
     * Returns merchant's completed appointments between now and an upper datetime limit
     */
    Collection<StyleRequest> findMerchantCompletedAppointments(Long merchantId, DateTime limit);


    /**
     * Returns customer's upcoming appointments between now and an upper datetime limit
     */
    Collection<StyleRequest> findCustomerAcceptedAppointments(Long customerId, DateTime limit);

    /**
     * Returns customer's cancelled appointments between now and an upper datetime limit
     */
    Collection<StyleRequest> findCustomerCancelledAppointments(Long customerId, DateTime limit);

    /**
     * Returns customer's pending appointments between now and an upper datetime limit
     */
    Collection<StyleRequest> findCustomerPendingAppointments(Long customerId, DateTime limit);

    /**
     * Returns customer's completed appointments between now and an upper datetime limit
     */
    Collection<StyleRequest> findCustomerCompletedAppointments(Long customerId, DateTime limit);

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
    void acceptStyleRequest(Long styleRequestId, Preferences preferences);
    void completeStyleRequest(Long styleRequestId, Preferences preferences);
    /**
     * Cancels a placed style request
     */
    void cancelStyleRequest(Long styleRequestId, Preferences preferences);
}
