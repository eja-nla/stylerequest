package com.hair.business.dao.datastore.abstractRepository;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.x.business.notif.Notification;

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

    StyleRequest findStyleRequest(Long id);

    /**
     * finds style requests booked by given customer
     */
    Collection<StyleRequest> findStyleRequests(List<Long> ids, StyleRequestState requestState);

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
    void saveCustomers(Collection<Customer> customers);

    Long saveStyle(Style style);

    Long saveStyleRequest(StyleRequest styleRequest);

    void saveStyleRequests(Collection<StyleRequest> styleRequests);

    Long saveNotification(Notification notification);

    void deleteCustomer(Customer cus);


}
