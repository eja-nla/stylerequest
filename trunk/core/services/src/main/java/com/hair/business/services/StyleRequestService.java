package com.hair.business.services;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.StyleRequest;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Style Service
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public interface StyleRequestService extends AppointmentFinder {

    /**
     * Finds stylerequest with given id
     */
    StyleRequest findStyleRequest(Long id);

    /**
     * Place a new style request for this customer
     * @param addOns List of AddOns that comes with the style request
     * @param styleId the ID of the style to be requested
     * @param customerId customer id
     * @param merchantId merchant id
     * @param appointmentTime when the customer will be styled
*
* Sets the location to the location of the merchant
**/
    StyleRequest placeStyleRequest(List<AddOn> addOns, Long styleId, Long customerId, Long merchantId, DateTime appointmentTime);

    /**
     * Update style request
     *
     * @param styleRequest the style request to be updated
     */
    void updateStyleRequest(StyleRequest styleRequest);


    /**
     * Accept a style request
     * @param styleRequestId stylerequest Id to be accepted
     * @param preferences user preferences, used for notifications etc
     */
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

    /**
     * Mark this request as no show i.e. it's end of appointment date but customer didn't show up
     */
    void markNoShow(Long styleRequestId, Preferences preferences);

}
