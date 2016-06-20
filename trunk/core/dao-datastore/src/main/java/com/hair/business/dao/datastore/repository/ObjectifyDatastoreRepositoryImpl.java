package com.hair.business.dao.datastore.repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.hair.business.beans.entity.Customer;
import com.hair.business.dao.datastore.abstractRepository.ObjectifyRepository;

import java.util.Collection;
import java.util.List;

/**
 * Objectify Datastore repository impl.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public class ObjectifyDatastoreRepositoryImpl implements ObjectifyRepository {
    /**
     * finds customer with given id
     */
    public Customer findCustomerNow(Long id) {
        return ofy().load().type(Customer.class).id(id).now();
    }

    /**
     * finds customers. There is no Ofy sync for this.
     */
    public Collection<Customer> findCustomers(List<Long> ids) {
        return ofy().load().type(Customer.class).ids(ids).values();
    }

    /**
     * saves a single customer information
     */
    public Long saveCustomerNow(Customer customer) {
        return ofy().save().entity(customer).now().getId();
    }

    /**
     * saves many customers
     */
    public void saveCustomersNow(Collection<Customer> customers) {
        ofy().save().entities(customers).now();
    }

}
