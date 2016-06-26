package com.hair.business.services.customer;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;

import java.util.Collection;

/**
 * Merchant Service.
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
public interface MerchantService {

    Merchant findMerchant(long id);

    Collection<Merchant> findMerchants(Collection<Long> ids);

    Collection<Merchant> findMerchantsByDescription(String description);

    Customer findCustomer(long id);

    Collection<Customer> findCustomers(Collection<Long> ids);

    Collection<Customer> findCustomersByDescription(String description);

    Style findStyle(long id);

    Collection<Style> findStyles(Collection<Long> ids);

    Collection<Style> findStylesByDescription(String description);

    void updateMerchant(Merchant merchant);

    void updateRequest(StyleRequest styleRequest);

    /**
     * Publish a new style that can be requested
     */
    void publishStyle(Style style, Merchant merchant);



    boolean cancelStyleRequest(Customer customer, StyleRequest request);

    boolean pay(Customer customer, Merchant merchant); //70440612
}
