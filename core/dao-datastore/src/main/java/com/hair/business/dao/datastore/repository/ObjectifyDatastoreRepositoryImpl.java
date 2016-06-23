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

    @Override
    public StyleRequest findStyleRequest(Long id) {
        return ofy().load().type(StyleRequest.class).id(id).now();
    }

    public Collection<StyleRequest> findStyleRequests(List<Long> ids, StyleRequestState requestState) {

        return ofy().load().type(StyleRequest.class).filter("state ==", requestState).list();
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
    public void saveCustomers(Collection<Customer> customers) {
        ofy().save().entities(customers);
    }

    public Long saveStyle(Style style) {
        return ofy().save().entity(style).now().getId();
    }

    public Long saveStyleRequest(StyleRequest styleRequest) {
        return ofy().save().entity(styleRequest).now().getId();
    }

    public void saveStyleRequests(Collection<StyleRequest> styleRequests) {
        ofy().save().entities(styleRequests);
    }

    public Long saveNotification(Notification notification) {
        return ofy().save().entity(notification).now().getId();
    }

    public void deleteCustomer(Customer cus) {
        // Not implemented. set active to false and update instead
    }

}
