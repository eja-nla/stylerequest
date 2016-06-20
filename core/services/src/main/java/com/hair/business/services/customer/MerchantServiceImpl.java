package com.hair.business.services.customer;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
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
public class MerchantServiceImpl implements MerchantService {

    static final Logger logger = Logger.getLogger(MerchantServiceImpl.class.getName());

    private final Repository repository;

    @Inject
    public MerchantServiceImpl(Repository repository) {
        this.repository = repository;
    }

    public Merchant findMerchant(long id) {
        return null;
    }

    public Collection<Merchant> findMerchants(Collection<Long> ids) {
        return null;
    }

    public Collection<Merchant> findMerchantsByDescription(String description) {
        return null;
    }

    public Customer findCustomer(long id) {
        return null;
    }

    public Customer findCustomer(Customer customer) {
        return null;
    }

    public Collection<Customer> findCustomers(Collection<Long> ids) {
        return null;
    }

    public Collection<Customer> findCustomersByDescription(String description) {
        return null;
    }

    public Style findStyle(long id) {
        return null;
    }

    public Collection<Style> findStyles(Collection<Long> ids) {
        return null;
    }

    public Collection<Style> findStylesByDescription(String description) {
        return null;
    }

    public void updateMerchant(Merchant merchant) {

    }

    public void updateRequest(StyleRequest styleRequest) {

    }

    public void publishStyle(Style style) {

    }

    public boolean cancelStyleRequest(Customer customer, StyleRequest request) {
        return false;
    }

    public boolean pay(Customer customer, Merchant merchant) {
        return false;
    }
}
