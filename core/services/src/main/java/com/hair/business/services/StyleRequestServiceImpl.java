package com.hair.business.services;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.payment.paypal.PaypalPaymentProcessor;
import com.hair.business.services.stereotype.Timed;
import com.x.business.notif.AcceptedStyleRequestNotification;
import com.x.business.notif.CancelledStyleRequestNotification;
import com.x.business.notif.CompletedStyleRequestNotification;
import com.x.business.notif.PlacedStyleRequestNotification;
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
    private final TaskQueue apnsQueue;
    private final PaypalPaymentProcessor paypalPaymentProcessor;

    private final List<String> MERCHANT_APPOINTMENTS_QUERY_CONDITIONS = Arrays.asList("merchantPermanentId ==", "state ==", "appointmentStartTime <=");
    private final List<String> CUSTOMER_APPOINTMENTS_QUERY_CONDITIONS = Arrays.asList("customerPermanentId ==", "state ==", "appointmentStartTime <=");

    private static final Logger logger = Logger.getLogger(StyleRequestServiceImpl.class.getName());

    @Inject
    StyleRequestServiceImpl(Repository repository,
                            @EmailTaskQueue TaskQueue emailTaskQueue,
                            @ApnsTaskQueue TaskQueue apnsQueue,
                            PaypalPaymentProcessor paypalPaymentProcessor) {
        this.repository = repository;
        this.emailTaskQueue = emailTaskQueue;
        this.apnsQueue = apnsQueue;
        this.paypalPaymentProcessor = paypalPaymentProcessor;
    }

    @Override
    public StyleRequest findStyleRequest(Long id) {
        Assert.validId(id);

        return repository.findOne(id, StyleRequest.class);
    }

    @Override
    public Collection<StyleRequest> findMerchantAcceptedAppointments(Long merchantId, DateTime untilWhen) {
        Assert.validId(merchantId);
        Assert.dateInFuture(untilWhen);

        List<Object> values = Arrays.asList(merchantId, StyleRequestState.ACCEPTED, untilWhen);

        return repository.findByQuery(StyleRequest.class, MERCHANT_APPOINTMENTS_QUERY_CONDITIONS, values);
    }

    @Override
    public Collection<StyleRequest> findMerchantCancelledAppointments(Long merchantId, DateTime timeAgo) {
        return findMerchantStyleRequests(merchantId, timeAgo, StyleRequestState.CANCELLED);
    }

    @Override
    public Collection<StyleRequest> findMerchantPendingAppointments(Long merchantId, DateTime timeAgo) {
        return findMerchantStyleRequests(merchantId, timeAgo, StyleRequestState.PENDING);
    }

    @Override
    public Collection<StyleRequest> findMerchantCompletedAppointments(Long merchantId, DateTime timeAgo) {
        return findMerchantStyleRequests(merchantId, timeAgo, StyleRequestState.COMPLETED);
    }

    @Override
    public Collection<StyleRequest> findCustomerAcceptedAppointments(Long customerId, DateTime upperDatelimit) {
        return findCustomerStyleRequests(customerId, upperDatelimit, StyleRequestState.ACCEPTED);
    }

    @Override
    public Collection<StyleRequest> findCustomerCancelledAppointments(Long customerId, DateTime upperDatelimit) {
        return findCustomerStyleRequests(customerId, upperDatelimit, StyleRequestState.CANCELLED);
    }

    @Override
    public Collection<StyleRequest> findCustomerPendingAppointments(Long customerId, DateTime upperDatelimit) {
        return findCustomerStyleRequests(customerId, upperDatelimit, StyleRequestState.PENDING);
    }

    @Override
    public Collection<StyleRequest> findCustomerCompletedAppointments(Long customerId, DateTime upperDatelimit) {
        return findCustomerStyleRequests(customerId, upperDatelimit, StyleRequestState.COMPLETED);
    }

    @Timed
    @Override
    public StyleRequest placeStyleRequest(Long styleId, Long customerId, Long merchantId, DateTime appointmentTime) {
        Assert.validIds(styleId, customerId, merchantId);
        Assert.dateInFuture(appointmentTime);

        Style style = repository.findOne(styleId, Style.class);
        Assert.isFound(style, String.format("Style with id %s not found", styleId));
        Customer customer = repository.findOne(customerId, Customer.class);
        Assert.isFound(style, String.format("Customer with id %s not found", customerId));
        Merchant merchant = repository.findOne(merchantId, Merchant.class);
        Assert.isFound(style, String.format("Merchant with id %s not found", merchantId));

        // TODO : further validations
        // is the merchant free at this time?
        // is the customer and merchant's country and city the same?
        // do we have this customer's sufficient payment info to make an authorization?

        style.setRequestCount(style.getRequestCount() + 1);

        final StyleRequest styleRequest = new StyleRequest(style, merchant, customer, merchant.getLocation(), StyleRequestState.PENDING, appointmentTime, new DateTime().plusMinutes(style.getDurationEstimate()));
        Long id = repository.allocateId(StyleRequest.class);
        styleRequest.setId(id);
        styleRequest.setPermanentId(id);


        repository.saveFew(styleRequest, style);

        // add paypal payment authorization request to a new payments queue


        emailTaskQueue.add(new PlacedStyleRequestNotification(styleRequest, merchant.getPreferences()));

//        Use feature toggle to turn this off or on instead of commenting
//        PushNotification pushNotification = new PushNotification()
//                .setAlert("New Style Request")
//                .setBadge(9)
//                .setSound("styleRequestSound.aiff")
//                .setDeviceTokens(customer.getDevice().getDeviceId()); // Nullable?
//        apnsQueue.add(new SendPushNotificationToApnsTask(pushNotification));

        logger.info("Placed Style Request. ID: " + styleRequest.getId());
        return styleRequest;
    }

    @Override
    public void updateStyleRequest(StyleRequest styleRequest) {
        repository.saveOne(styleRequest);
    }

    @Override
    public void acceptStyleRequest(Long styleRequestId, Preferences preferences) {
        Assert.validId(styleRequestId);
        StyleRequest styleRequest = transitionStyleRequest(styleRequestId, StyleRequestState.ACCEPTED);

        emailTaskQueue.add(new AcceptedStyleRequestNotification(styleRequest, preferences));
    }

    @Override
    public void completeStyleRequest(Long styleRequestId, Preferences preferences) {
        StyleRequest styleRequest = transitionStyleRequest(styleRequestId, StyleRequestState.COMPLETED);

        emailTaskQueue.add(new CompletedStyleRequestNotification(styleRequest, preferences));
    }

    @Override
    public void cancelStyleRequest(Long styleRequestId, Preferences preferences) {
        StyleRequest styleRequest = transitionStyleRequest(styleRequestId, StyleRequestState.CANCELLED);

        //TODO notify merchant
        emailTaskQueue.add(new CancelledStyleRequestNotification(styleRequest, preferences));

    }

    private Collection<StyleRequest> findMerchantStyleRequests(Long id, DateTime untilWhen, StyleRequestState state) {
        List<Object> values = validate(id, untilWhen, state);

        return repository.findByQuery(StyleRequest.class, MERCHANT_APPOINTMENTS_QUERY_CONDITIONS, values);
    }
    private Collection<StyleRequest> findCustomerStyleRequests(Long id, DateTime untilWhen, StyleRequestState state) {
        List<Object> values = validate(id, untilWhen, state);

        return repository.findByQuery(StyleRequest.class, CUSTOMER_APPOINTMENTS_QUERY_CONDITIONS, values);
    }

    private List<Object> validate(Long id, DateTime untilWhen, StyleRequestState state) {
        Assert.validId(id);
        Assert.dateInFuture(untilWhen);

        return Arrays.asList(id, state, untilWhen);
    }

    private StyleRequest transitionStyleRequest(Long id, StyleRequestState state) {
        Assert.validId(id);
        StyleRequest styleRequest = repository.findOne(id, StyleRequest.class);
        Assert.notNull(styleRequest, String.format("Style request with ID %s not found", styleRequest));
        styleRequest.setState(state);

        updateStyleRequest(styleRequest);

        return styleRequest;
    }

}
