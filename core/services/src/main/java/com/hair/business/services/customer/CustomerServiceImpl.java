package com.hair.business.services.customer;

import static org.joda.time.DateTime.now;

import com.hair.business.beans.constants.NotificationType;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.pushNotification.SendPushNotificationToApnsTask;
import com.x.business.notif.Notification;
import com.x.business.scheduler.TaskQueue;
import com.x.business.scheduler.stereotype.ApnsTaskQueue;
import com.x.business.scheduler.stereotype.EmailTaskQueue;

import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import apns.PushNotification;

/**
 * Customer Service Impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
public class CustomerServiceImpl implements CustomerService {

    static final Logger logger = Logger.getLogger(CustomerServiceImpl.class.getName());
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
    public StyleRequest placeStyleRequest(Long styleId, Long customerId, Long merchantId, DateTime appointmentTime) {

        Style style = repository.findOne(styleId, Style.class);
        Customer customer = repository.findOne(customerId, Customer.class);
        Merchant merchant = repository.findOne(merchantId, Merchant.class);

        style.setRequestCount(style.getRequestCount() + 1);

        Long id = repository.allocateId(StyleRequest.class);
        StyleRequest styleRequest = new StyleRequest(style, merchant, customer, merchant.getLocation(), StyleRequestState.PENDING, now());
        styleRequest.setId(id);

        repository.saveFew(styleRequest, style);

        emailTaskQueue.add(new Notification(styleRequest, NotificationType.PUSH_EMAIL));

        PushNotification pushNotification = new PushNotification()
                .setAlert("New Style Request")
                .setBadge(9)
                .setSound("styleRequestSound.aiff")
                .setDeviceTokens(customer.getDevice().getDeviceId()); // Nullable?
        apnsQueue.add(new SendPushNotificationToApnsTask(pushNotification));

        return styleRequest;
    }

    @Override
    public void cancelStyleRequest(Customer customer, Merchant merchant, StyleRequest styleRequest) {
        styleRequest.setState(StyleRequestState.CANCELLED);
        repository.saveOne(styleRequest);

        //TODO notify merchant
        emailTaskQueue.add(new Notification(styleRequest, NotificationType.PUSH_EMAIL));

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
