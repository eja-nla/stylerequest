package com.hair.business.dao.datastore.abstractRepository;

import com.hair.business.beans.entity.Customer;

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
     * saves a single customer information
     * */
    Long saveCustomerNow(Customer customer);

    /**
     * saves many customers
     * */
    void saveCustomersNow(Collection<Customer> customers);

}
