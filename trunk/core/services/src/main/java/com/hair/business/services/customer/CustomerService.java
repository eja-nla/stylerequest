package com.hair.business.services.customer;


import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Payment;
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
     * */
    Customer createCustomer(String firstname, String lastname, String email, String phone, Device device, Location location);

    /**
     * Updates customer payment information
     * */
    Payment updatePaymentInfo(Long customerId, Payment payment);

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
    void deactivateCustomer(Customer customer);

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

}
