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
     * Returns merchant's accepted appointments (whose start time is) between now and an upper datetime limit
     * // todo could this rather be 'between 2 given dates' ?
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
     * @param styleId the ID of the style to be requested
     * @param customerId customer id
     * @param merchantId merchant id
     * @param appointmentTime when the customer will be styled
     *
     * Sets the location to the location of the merchant
     *
     * Do paypal authorization here.
     * Note to self: because paypal guarantees capture for only the first 3 days of authorization period,
     *               if the style is placed > 3 days in advance, we might have to reauthorize within
     *               a 3 day period of the request date to guarantee payment capture on the fulfillment date
     */
    StyleRequest placeStyleRequest(Long styleId, Long customerId, Long merchantId, DateTime appointmentTime);

    void updateStyleRequest(StyleRequest styleRequest);
    void acceptStyleRequest(Long styleRequestId, Preferences preferences);

    /**
     * Completes the request cycle.
     *
     * Must ensure payment is settled. For paypal payments, we do the authorization capture here.
     * */
    void completeStyleRequest(Long styleRequestId, Preferences preferences);
    /**
     * Cancels a placed style request
     */
    void cancelStyleRequest(Long styleRequestId, Preferences preferences);
}
