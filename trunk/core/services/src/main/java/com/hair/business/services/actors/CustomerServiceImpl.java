package com.hair.business.services.actors;

import com.hair.business.dao.entity.Customer;
import com.hair.business.dao.entity.Merchant;
import com.hair.business.dao.entity.Payment;
import com.hair.business.dao.entity.StyleRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

/**
 * Customer Service Impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
public class CustomerServiceImpl implements CustomerService {

    static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    public boolean saveCustomer(Customer customer) {
        return false;
    }

    @Cacheable(value = "customerCache", key = "#customer.getId()")
    public Customer findCustomer(Customer customer) {
        return null;
    }

    @Cacheable(value = "customerCache", key = "#customer.getId()")
    public Collection<StyleRequest> findCustomerStyleRequests(Customer customer) {
        return null;
    }

    @CacheEvict(value = "customerCache", key = "#customer.getId()")
    public void deactivateCustomer(Customer customer) {

    }

    public boolean placeStyleRequest(Customer customer, Merchant merchant) {
        return false;
    }

    public boolean cancelStyleRequest(Customer customer, StyleRequest request) {
        return false;
    }

    public Payment pay(Customer customer, Merchant merchant) {
        return null;
    }

    public Customer save(Customer bean) {
        return null;
    }

    public void delete(Customer bean) {

    }

    @Cacheable(cacheNames = {"customerCache"})
    public Customer find(String s) {
        return null;
    }

    @Cacheable(cacheNames = {"customerCache"})
    public Iterable<Customer> find() {
        return null;
    }
}
