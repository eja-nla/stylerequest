package com.hair.business.services.customer;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.AbstractAsyncRepository;
import com.hair.business.dao.datastore.abstractRepository.AbstractSyncRepository;

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

    private final AbstractSyncRepository syncRepository;
    private final AbstractAsyncRepository asyncRepository;

    @Inject
    public CustomerServiceImpl(AbstractSyncRepository syncRepository, AbstractAsyncRepository asyncRepository) {
        this.syncRepository = syncRepository;
        this.asyncRepository = asyncRepository;

    }

    public boolean saveCustomer(Customer customer) {
        return false;
    }

    public Customer findCustomer(Customer customer) {
        return null;
    }

    public Collection<StyleRequest> findStyleRequests(Customer customer) {
        return null;
    }

    public void deactivateCustomer(Customer customer) {

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
