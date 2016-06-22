package com.hair.business.dao.datastore.repository;


import static com.hair.business.dao.datastore.ofy.OfyService.ofy;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.ObjectifyRepository;
import com.x.business.notif.Notification;

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

    public Collection<StyleRequest> findStyleRequests(Long entityId, StyleRequestState requestState) {
        return null;
    }

    public Style findStyle(Long id) {
        return ofy().load().type(Style.class).id(id).now();
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

    public Long saveStyle(Style style) {
        return ofy().save().entity(style).now().getId();
    }

    public void saveStyleRequest(StyleRequest styleRequest) {

    }

    public void saveNotification(Notification notification) {

    }

}
