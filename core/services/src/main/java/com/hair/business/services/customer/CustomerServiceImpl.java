package com.hair.business.services.customer;

import static org.joda.time.DateTime.*;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;

import org.joda.time.DateTime;

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

    public Customer findCustomer(Long id) {
        return repository.findCustomerNow(id);
    }

    public void saveCustomer(Customer customer) {
        repository.saveCustomerNow(customer);
    }

    public Collection<StyleRequest> findStyleRequests(Long customerId, StyleRequestState styleRequestState) {

        return repository.findStyleRequests(customerId, styleRequestState);
    }

    public void deactivateCustomer(Customer customer) {

        customer.setActive(false);
        repository.saveCustomerNow(customer);

    }

    public boolean placeStyleRequest(Style style, Customer customer, Merchant merchant, Location location, DateTime dateTime) {
        //create new request
        // increment style counter
        // TODO notify merchant via task queue

        style.setRequestCount(style.getRequestCount() + 1);
        StyleRequest styleRequest = new StyleRequest(style, merchant.getId(), customer.getId(), location, StyleRequestState.PENDING, now());

        repository.saveStyle(style);
        repository.saveStyleRequest(styleRequest);

        return true;
    }

    public boolean cancelStyleRequest(Customer customer, StyleRequest styleRequest) {
        styleRequest.setState(StyleRequestState.CANCELLED);
        repository.saveStyleRequest(styleRequest);
        //TODO notify merchant

        return false;
    }

    public boolean pay(Customer customer, Merchant merchant) {
        return false;
    }

    public Collection<Image> findTrendingStyles(Location location) {
        return null;
    }

    public void contactMerchant(Long merchantId, String message) {

    }


}
