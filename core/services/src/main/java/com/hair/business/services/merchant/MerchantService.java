package com.hair.business.services.merchant;

import com.hair.business.beans.entity.Address;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
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
    void createMerchant(String firstName, String lastname, String displayName, String email, String phone, Device device, Address address);

    void updateMerchant(Merchant merchant);

    void pay(Customer customer, Merchant merchant);

    /**
     * Is the merchant booked during this period? We provide a guess here.
     *
     * Returns true if merchant has at least 1 appointment starting ~30 minutes before requested period
     *
     * Ideally, we'd want to check if that time lies between start and
     * end time of the appointment but objectify has a unitary inequality search restriction on entity fields
     * */
    boolean isBooked(Long merchantId, DateTime startTime, DateTime endTime);

}
