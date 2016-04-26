package com.hair.business.services.actors;


import com.hair.business.dao.entity.Customer;
import com.hair.business.dao.entity.Merchant;
import com.hair.business.dao.entity.Payment;
import com.hair.business.dao.entity.StyleRequest;
import com.hair.business.services.stereotype.PersistenceService;

import java.util.Collection;

/**
 * Consumer Service.
 *
 * Created by Olukorede Aguda on 25/04/2016.
 */
public interface ConstomerService extends PersistenceService<Customer, String> {

    boolean saveCustomer(Customer customer);

    Customer findCustomer(Customer customer);

    Collection<StyleRequest> findCustomerStyleRequests(Customer customer);

    Collection<StyleRequest> findMerchantStyleRequests(Customer customer);

    boolean placeStyleRequest(Customer customer, Merchant merchant);

    boolean cancelStyleRequest(Customer customer, StyleRequest request);

    Payment pay(Customer customer, Merchant merchant);

}
