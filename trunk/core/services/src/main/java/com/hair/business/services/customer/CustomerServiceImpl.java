package com.hair.business.services.customer;

import static org.joda.time.DateTime.now;

import com.google.appengine.repackaged.com.google.common.collect.Lists;

import com.hair.business.beans.constants.NotificationType;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.business.notif.Notification;

import org.joda.time.DateTime;

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

    static final Logger logger = Logger.getLogger(CustomerServiceImpl.class.getName());
    private final Repository repository;

    @Inject
    public CustomerServiceImpl(Repository repository) {
        this.repository = repository;

    }

    @Override
    public Customer findCustomer(Long id) {
        return repository.findOne(id, Customer.class);
    }

    @Override
    public void saveCustomer(Customer customer) {
        repository.saveOne(customer);
    }

    @Override
    public Collection<StyleRequest> findStyleRequests(List<Long> customerId, StyleRequestState styleRequestState) {

        return repository.findByQuery(customerId, StyleRequest.class, "state ==", styleRequestState);
    }

    @Override
    public void deactivateCustomer(Customer customer) {

        customer.setActive(false);
        repository.saveOne(customer);

    }

    @Override
    public void placeStyleRequest(Style style, Customer customer, Merchant merchant, Location location, DateTime dateTime) {

        style.setRequestCount(style.getRequestCount() + 1);

        Long id = repository.allocateId(StyleRequest.class);
        StyleRequest styleRequest = new StyleRequest(style, merchant, customer, location, StyleRequestState.PENDING, now());
        styleRequest.setId(id);

        repository.saveMany(Lists.newArrayList(styleRequest, style));

        new Notification<>(styleRequest, NotificationType.EMAIL).schedule();

    }

    @Override
    public void cancelStyleRequest(Customer customer, Merchant merchant, StyleRequest styleRequest) {
        styleRequest.setState(StyleRequestState.CANCELLED);
        repository.saveOne(styleRequest);

        //TODO notify merchant
        new Notification<>(styleRequest, NotificationType.PUSH_EMAIL);

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
