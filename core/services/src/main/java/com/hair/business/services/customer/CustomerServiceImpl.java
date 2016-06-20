package com.hair.business.services.customer;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;

import java.util.Collection;
import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Customer Service Impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
public class CustomerServiceImpl implements CustomerService {

    static final Logger logger = Logger.getLogger(CustomerServiceImpl.class.getName());
    private final Repository repository;


    @Inject
    public CustomerServiceImpl(Repository repository) {
        this.repository = repository;

    }

    public boolean saveCustomer(Customer customer) {

        repository.saveCustomerNow(customer);
        return true;
    }

    public Customer findCustomer(Long id ) {
        return repository.findCustomerNow(id);
    }

    public Collection<StyleRequest> findStyleRequests(Customer customer) {
        return null;
    }

    public void deactivateCustomer(Customer customer) {
        customer.setActive(false);
        repository.saveCustomerNow(customer);
    }

    public boolean placeStyleRequest(Customer customer, Merchant merchant) {
        return false;
    }

    public boolean cancelStyleRequest(Customer customer, StyleRequest request) {
        return false;
    }

    public boolean pay(Customer customer, Merchant merchant) {
        return false;
    }

    public void delete(Customer bean) {

    }

    public Customer find(String s) {
        return null;
    }

    public Iterable<Customer> find() {
        return null;
    }
}
