package com.hair.business.services;

import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.payment.PaymentService;
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
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    private final PaymentService paymentService;

    private static final List<String> MERCHANT_APPOINTMENTS_QUERY_CONDITIONS = Arrays.asList("merchantPermanentId ==", "state ==", "appointmentStartTime >=", "appointmentStartTime <=");
    private static final List<String> CUSTOMER_APPOINTMENTS_QUERY_CONDITIONS = Arrays.asList("customerPermanentId ==", "state ==", "appointmentStartTime >=", "appointmentStartTime <=");

    private final Logger logger = getLogger(this.getClass());

    @Inject
    StyleRequestServiceImpl(Repository repository,
                            @EmailTaskQueue TaskQueue emailTaskQueue,
                            @ApnsTaskQueue TaskQueue apnsQueue,
                            PaymentService paymentService) {
        this.repository = repository;
        this.emailTaskQueue = emailTaskQueue;
        this.apnsQueue = apnsQueue;
        this.paymentService = paymentService;
    }

    @Override
    public StyleRequest findStyleRequest(Long id) {
        Assert.validId(id);

        return repository.findOne(id, StyleRequest.class);
    }

    @Override
    public List<StyleRequest> findMerchantAcceptedAppointments(Long merchantId, DateTime lower, DateTime upper) {
        return findMerchantStyleRequests(merchantId, lower, upper, StyleRequestState.ACCEPTED);
    }

    @Override
    public List<StyleRequest> findMerchantCancelledAppointments(Long merchantId, DateTime lower, DateTime upper) {
        return findMerchantStyleRequests(merchantId, lower, upper, StyleRequestState.CANCELLED);
    }

    @Override
    public List<StyleRequest> findMerchantPendingAppointments(Long merchantId, DateTime lower, DateTime upper) {
        return findMerchantStyleRequests(merchantId, lower, upper, StyleRequestState.PENDING);
    }

    @Override
    public List<StyleRequest> findMerchantCompletedAppointments(Long merchantId, DateTime lower, DateTime upper) {
        return findMerchantStyleRequests(merchantId, lower, upper, StyleRequestState.COMPLETED);
    }

    @Override
    public Collection<StyleRequest> findCustomerAcceptedAppointments(Long customerId, DateTime lower, DateTime upper) {
        return findCustomerStyleRequests(customerId, lower, upper, StyleRequestState.ACCEPTED);
    }

    @Override
    public List<StyleRequest> findCustomerCancelledAppointments(Long customerId, DateTime lower, DateTime upper) {
        return findCustomerStyleRequests(customerId, lower, upper, StyleRequestState.CANCELLED);
    }

    @Override
    public List<StyleRequest> findCustomerPendingAppointments(Long customerId, DateTime lower, DateTime upper) {
        return findCustomerStyleRequests(customerId, lower, upper, StyleRequestState.PENDING);
    }

    @Override
    public List<StyleRequest> findCustomerCompletedAppointments(Long customerId, DateTime lower, DateTime upper) {
        return findCustomerStyleRequests(customerId, lower, upper, StyleRequestState.COMPLETED);
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

        final StyleRequest styleRequest = new StyleRequest(style, merchant, customer, merchant.getAddress().getLocation(), StyleRequestState.PENDING, appointmentTime, new DateTime().plusMinutes(style.getDurationEstimate()));
        Long id = repository.allocateId(StyleRequest.class);
        styleRequest.setId(id);
        styleRequest.setPermanentId(id);

        // add payment authorization request to a new payments queue
        // paymentService.holdPayment(styleRequest, customer); //fixme uncomment before deployment TLS v1.2 issues, GAE says it works in prod but not dev, idiots

        emailTaskQueue.add(new PlacedStyleRequestNotification(styleRequest, merchant.getPreferences()));

//        Use feature toggle to turn this off or on instead of commenting
//        PushNotification pushNotification = new PushNotification()
//                .setAlert("New Style Request")
//                .setBadge(9)
//                .setSound("styleRequestSound.aiff")
//                .setDeviceTokens(customer.getDevice().getDeviceId()); // Nullable?
//        apnsQueue.add(new SendPushNotificationToApnsTask(pushNotification));

        repository.saveFew(styleRequest, style);
        logger.debug("Placed Style Request. ID: " + styleRequest.getId());
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

    private List<StyleRequest> findMerchantStyleRequests(Long id, DateTime start, DateTime stop, StyleRequestState state) {
        List<Object> values = validate(id, state, start, stop);

        return repository.findByQuery(StyleRequest.class, MERCHANT_APPOINTMENTS_QUERY_CONDITIONS, values);
    }

    private List<StyleRequest> findCustomerStyleRequests(Long id, DateTime start, DateTime stop, StyleRequestState state) {
        List<Object> values = validate(id, state, start, stop);

        return repository.findByQuery(StyleRequest.class, CUSTOMER_APPOINTMENTS_QUERY_CONDITIONS, values);
    }

    private List<Object> validate(Long id, StyleRequestState state, DateTime start, DateTime stop) {
        Assert.validId(id);

        return Arrays.asList(id, state, start, stop);
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
