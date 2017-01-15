package com.hair.business.services;

import com.hair.business.beans.constants.NotificationType;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Style Request Service impl
 *
 * Created by Olukorede Aguda on 07/01/2017.
 */
public class StyleRequestServiceImpl implements StyleRequestService {

    private final Repository repository;
    private final TaskQueue emailTaskQueue;

    private final List<String> APPOINTMENTS_QUERY_CONDITIONS = Arrays.asList("merchantPermanentId ==", "state ==", "appointmentDateTime >");

    private static final Logger logger = Logger.getLogger(StyleRequestServiceImpl.class.getName());

    @Inject
    public StyleRequestServiceImpl(Repository repository, @EmailTaskQueue TaskQueue emailTaskQueue, @ApnsTaskQueue TaskQueue apnsQueue) {
        this.repository = repository;
        this.emailTaskQueue = emailTaskQueue;
    }

    @Override
    public StyleRequest findStyleRequest(Long id) {
        return null;
    }

    @Override
    public Collection<StyleRequest> findUpcomingAppointments(Long merchantId, DateTime timeAgo) {
        List<Object> values = Arrays.asList(merchantId, StyleRequestState.ACCEPTED, timeAgo);

        return repository.findByQuery(StyleRequest.class, APPOINTMENTS_QUERY_CONDITIONS, values);
    }

    @Override
    public Collection<StyleRequest> findCancelledAppointments(Long merchantId, DateTime timeAgo) {
        List<Object> values = Arrays.asList(merchantId, StyleRequestState.CANCELLED, timeAgo);

        return repository.findByQuery(StyleRequest.class, APPOINTMENTS_QUERY_CONDITIONS, values);
    }

    @Override
    public Collection<StyleRequest> findPendingAppointments(Long merchantId, DateTime timeAgo) {
        List<Object> values = Arrays.asList(merchantId, StyleRequestState.PENDING, timeAgo);

        return repository.findByQuery(StyleRequest.class, APPOINTMENTS_QUERY_CONDITIONS, values);
    }

    @Override
    public Collection<StyleRequest> findCompletedAppointments(Long merchantId, DateTime timeAgo) {
        List<Object> values = Arrays.asList(merchantId, StyleRequestState.COMPLETED, timeAgo);

        return repository.findByQuery(StyleRequest.class, APPOINTMENTS_QUERY_CONDITIONS, values);
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

        logger.info("Successfully placed Style Request. ID: " + styleRequest.getId());
        return styleRequest;
    }

    @Override
    public void updateStyleRequest(StyleRequest styleRequest) {
        repository.saveOne(styleRequest);
    }

    @Override
    public void cancelStyleRequest(StyleRequest styleRequest) {
        styleRequest.setState(StyleRequestState.CANCELLED);
        updateStyleRequest(styleRequest);

        //TODO notify merchant
        emailTaskQueue.add(new Notification(styleRequest, NotificationType.PUSH_EMAIL));

    }

}
