package com.hair.business.services.customer;

import static java.util.logging.Logger.getLogger;

import com.hair.business.beans.constants.NotificationType;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.business.notif.Notification;
import com.x.business.scheduler.TaskQueue;
import com.x.business.scheduler.stereotype.ApnsTaskQueue;
import com.x.business.scheduler.stereotype.EmailTaskQueue;
import com.x.business.utilities.Assert;

import org.joda.time.DateTime;

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
    public StyleRequest placeStyleRequest(Long styleId, Long customerId, Long merchantId, DateTime appointmentTime) {

        Style style = repository.findOne(styleId, Style.class);
        Assert.isFound(style, String.format("Style with id %s not found", styleId));
        Customer customer = repository.findOne(customerId, Customer.class);
        Assert.isFound(style, String.format("Customer with id %s not found", customerId));
        Merchant merchant = repository.findOne(merchantId, Merchant.class);
        Assert.isFound(style, String.format("Merchant with id %s not found", merchantId));

        style.setRequestCount(style.getRequestCount() + 1);

        StyleRequest styleRequest = new StyleRequest(style, merchant, customer, merchant.getLocation(), StyleRequestState.PENDING, appointmentTime);
        Long id = repository.allocateId(StyleRequest.class);
        styleRequest.setId(id);
        styleRequest.setPermanentId(id);


        repository.saveFew(styleRequest, style);

        emailTaskQueue.add(new Notification(styleRequest, NotificationType.PUSH_EMAIL));

//        Use feature toggle to turn this off or on instead of commenting
//        PushNotification pushNotification = new PushNotification()
//                .setAlert("New Style Request")
//                .setBadge(9)
//                .setSound("styleRequestSound.aiff")
//                .setDeviceTokens(customer.getDevice().getDeviceId()); // Nullable?
//        apnsQueue.add(new SendPushNotificationToApnsTask(pushNotification));

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
