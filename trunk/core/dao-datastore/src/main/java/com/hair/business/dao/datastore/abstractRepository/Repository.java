package com.hair.business.dao.datastore.abstractRepository;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;

import java.util.Collection;
import java.util.List;

/**
 * Abstract Repository interface
 *
 * Underlying impl can simply be changed by making this interface extend a new Abstract{New impl}SyncRepository wrapper
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface Repository {

    /**
     * finds customer with given id sync
     * */
    Customer findCustomerNow(Long id);

    /**
     * finds customers. There is no Ofy sync for this.
     * */
    Collection<Customer> findCustomers(List<Long> ids);

    /**
     * finds style requests booked by given customer
     */
    Collection<StyleRequest> findStyleRequests(Long entityId, StyleRequestState requestState);

    /**
     * Finds details for a given style Id
     */
    Style findStyle(Long id);

    /**
     * saves a single customer information
     * */
    Long saveCustomerNow(Customer customer);

    /**
     * saves many customers
     * */
    void saveCustomersNow(Collection<Customer> customers);

    Long saveStyle(Style style);

    void saveStyleRequest(StyleRequest styleRequest);
}
