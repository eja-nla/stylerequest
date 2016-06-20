package com.hair.business.services.customer;


import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequest;

import java.util.Collection;

/**
 * Consumer Service.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface CustomerService {

    boolean saveCustomer(Customer customer);

    Customer findCustomer(Long id);

    Collection<StyleRequest> findStyleRequests(Customer customer);

    void deactivateCustomer(Customer customer);

    boolean placeStyleRequest(Customer customer, Merchant merchant);

    boolean cancelStyleRequest(Customer customer, StyleRequest request);

    boolean pay(Customer customer, Merchant merchant); //70440612

}
