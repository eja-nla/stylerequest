package com.hair.business.services.customer;


import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;

import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;

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
     * Saves new customer
     * */
    void saveCustomer(Customer customer);

    /**
     * Finds style requests by this customer in the given state
     */
    Collection<StyleRequest> findStyleRequests(List<Long> customerId, StyleRequestState styleRequestState);

    /**
     * Deactivates customer account
     */
    void deactivateCustomer(Customer customer);

    /**
     * Place a new style request for this customer
     */
    boolean placeStyleRequest(Style style, Customer customer, Merchant merchant, Location location, DateTime dateTime);

    /**
     * Cancels a placed request by this customer
     */
    boolean cancelStyleRequest(Customer customer, Merchant merchant, StyleRequest request);

    /**
     *  Issues a payment request from a customer to a merchant
     */
    boolean pay(Customer customer, Merchant merchant);

    /**
     * Fetches trending styles for this user based on location
     */
    Collection<Style> findTrendingStyles(Location location);

    /**
     * Contacts the given merchant with given message
     */
    void contactMerchant(Long merchantId, String message);


}
