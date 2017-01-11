package com.hair.business.services.customer;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
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

    /**
     * Creates a new Merchant
     * */
    void createMerchant(String name, String email, String phone, Device device, Location location);

    void updateMerchant(Merchant merchant);

    void updateRequest(StyleRequest styleRequest);

    void cancelStyleRequest(Customer customer, StyleRequest request);

    void pay(Customer customer, Merchant merchant);

}
