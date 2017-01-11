package com.hair.business.services.customer;

import static java.util.logging.Logger.getLogger;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.business.scheduler.TaskQueue;
import com.x.business.scheduler.stereotype.ApnsTaskQueue;
import com.x.business.scheduler.stereotype.EmailTaskQueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Customer Service Impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
public class CustomerServiceImpl implements CustomerService {

    static final Logger logger = getLogger(CustomerServiceImpl.class.getName());
    private final Repository repository;
    private final TaskQueue emailTaskQueue;
    private final TaskQueue apnsQueue;

    @Inject
    public CustomerServiceImpl(Repository repository, @EmailTaskQueue TaskQueue emailTaskQueue, @ApnsTaskQueue TaskQueue apnsQueue) {
        this.repository = repository;
        this.emailTaskQueue = emailTaskQueue;
        this.apnsQueue = apnsQueue;

    }

    @Override
    public Customer findCustomer(Long id) {
        return repository.findOne(id, Customer.class);
    }


    @Override
    public void createCustomer(String name, String email, String phone, Device device, Location location) {
        Long permId = repository.allocateId(Customer.class);
        Customer customer = new Customer(name, 0, email, phone, device, location);
        customer.setId(permId);
        customer.setPermanentId(permId);
        saveCustomer(customer);
    }


    @Override
    public void saveCustomer(Customer customer) {
        repository.saveOne(customer);
    }

    @Override
    public Collection<StyleRequest> findStyleRequests(Long permanentId, StyleRequestState styleRequestState) {
        List<String> conditions = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        conditions.add("customerPermanentId");
        conditions.add("state");
        values.add(permanentId);
        values.add(styleRequestState);
        return repository.findByQuery(StyleRequest.class, conditions, values);
    }

    @Override
    public void deactivateCustomer(Customer customer) {

        customer.setActive(false);
        repository.saveOne(customer);

    }

    @Override
    public void pay(Customer customer, Merchant merchant) {
        //TODO implement
    }

    @Override
    public Collection<Style> findTrendingStyles(Location location) {
        //TODO implement
        return null;
    }

    @Override
    public void contactMerchant(Long merchantId, String message) {

    }

}
