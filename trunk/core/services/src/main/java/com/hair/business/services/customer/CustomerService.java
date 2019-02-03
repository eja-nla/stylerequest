package com.hair.business.services.customer;


import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;

import java.util.Collection;

/**
 * Consumer Service.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface CustomerService {

    /**
     * Finds customer with given id*/
    Customer findCustomer(Long id);

    /**
     * Finds customer with given email
     * */
    Customer findCustomer(String email);

    /**
     * Creates a new customer
     *
     * During sign-up:
     *  Client app comes and gets a client token, presenting an empty ID
     *      we issue one. Client uses that token to obtain a nonce from Payment gateway
     *  We take customer's basic details through the client and nonce
     *      we present that to the server here and create a payment profile
     *  Then finally, we save the user entity
     *  It is expected that before the user can subsequently do things requiring payment, the client will
     *      direct user to payment gateway to collect their payment details and come back to us here with a
     * */
    String createCustomer(Customer customer, String nonce);

    /**
     * Saves new customer
     * */
    void saveCustomer(Customer customer);

    /**
     * Finds style requests by this customer in the given state
     */
    Collection<StyleRequest> findStyleRequests(Long customerId, StyleRequestState styleRequestState);

    /**
     * Deactivates customer account
     */
    void deactivateCustomer(Long customerId);

    /**
     *  Issues a payment request from a customer to a merchant
     */
    void pay(Customer customer, Merchant merchant);

    /**
     * Fetches trending styles for this user based on location
     */
    Collection<Style> findTrendingStyles(Location location);

    /**
     * Contacts the given merchant with given message
     */
    void contactMerchant(Long merchantId, String message);

    void updateRating(Long customerId, int score);

    Preferences updatePreferences(Long customerId, Preferences preferences);
}
