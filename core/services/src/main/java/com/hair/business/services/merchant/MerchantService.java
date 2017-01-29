package com.hair.business.services.merchant;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;

import org.joda.time.DateTime;

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

    void pay(Customer customer, Merchant merchant);

    /**
     * Is the merchant booked during this period?
     *
     * */
    boolean isBooked(Merchant merchant, DateTime period);

    void acceptStyleRequest(Long merchantId, Long styleRequestId);

}
