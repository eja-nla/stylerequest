package com.hair.business.services.actors;

import com.hair.business.cache.repository.WriteBehind;
import com.hair.business.dao.entity.Customer;
import com.hair.business.dao.entity.Merchant;
import com.hair.business.dao.entity.Payment;
import com.hair.business.dao.entity.StyleRequest;
import com.hair.business.dao.es.repository.CustomerRepository;
import com.hair.business.services.AbstractPersistenceService;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Customer Service Impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
@Named
public class CustomerServiceImpl extends AbstractPersistenceService<CustomerRepository, Customer, String> implements CustomerService {

    static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository cRepository;
    private final WriteBehind asyncPersistor;

    @Inject
    public CustomerServiceImpl(CustomerRepository cRepository, WriteBehind asyncPersistor) {
        super(cRepository);

        this.cRepository = cRepository;
        this.asyncPersistor = asyncPersistor;
    }

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

    public void delete(Customer bean) {

    }

    @Cacheable(cacheNames = {"customerCache"})
    public Customer find(String s) {
        Customer x = new Customer();
        x.setId("xId"); x.setCreated(DateTime.now());
        return x;
    }

    @Cacheable(cacheNames = {"customerCache"})
    public Iterable<Customer> find() {
        return null;
    }
}
