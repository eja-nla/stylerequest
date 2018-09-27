package com.hair.business.services;

import static com.hair.business.beans.constants.StyleRequestState.ACCEPTED;
import static com.hair.business.beans.constants.StyleRequestState.CANCELLED;
import static com.hair.business.beans.constants.StyleRequestState.COMPLETED;
import static com.hair.business.beans.constants.StyleRequestState.PENDING;
import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.merchant.MerchantService;
import com.hair.business.services.payment.PaymentService;
import com.hair.business.services.state.StylerequestStateMgr;
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

import javax.inject.Inject;

/**
 * Style Request Service impl
 *
 * Created by Olukorede Aguda on 07/01/2017.
 */
public class StyleRequestServiceImpl extends AppointmentFinderExt implements StyleRequestService {

    private final Repository repository;
    private final TaskQueue emailTaskQueue;
    private final TaskQueue apnsQueue;
    private final PaymentService paymentService;
    private final MerchantService merchantService;
    private final StylerequestStateMgr stateMgr;

    private static final Logger logger = getLogger(StyleRequestServiceImpl.class);

    @Inject
    StyleRequestServiceImpl(Repository repository,
                            @EmailTaskQueue TaskQueue emailTaskQueue,
                            @ApnsTaskQueue TaskQueue apnsQueue,
                            PaymentService paymentService, MerchantService merchantService, StylerequestStateMgr stateMgr) {
        super(repository);
        this.repository = repository;
        this.emailTaskQueue = emailTaskQueue;
        this.apnsQueue = apnsQueue;
        this.paymentService = paymentService;
        this.merchantService = merchantService;
        this.stateMgr = stateMgr;
    }

    @Timed
    @Override
    public StyleRequest findStyleRequest(Long id) {
        Assert.validId(id);

        return repository.findOne(id, StyleRequest.class);
    }


    @Timed
    @Override
    public StyleRequest placeStyleRequest(Long styleId, Long customerId, Long merchantId, DateTime appointmentTime) {
        Assert.validIds(styleId, customerId, merchantId);
        Assert.dateInFuture(appointmentTime);

        final Style style = repository.findOne(styleId, Style.class);
        Assert.notNull(style, String.format("Style with id %s not found", styleId));
        final Customer customer = repository.findOne(customerId, Customer.class);
        Assert.notNull(style, String.format("Customer with id %s not found", customerId));
        final Merchant merchant = repository.findOne(merchantId, Merchant.class);
        Assert.notNull(style, String.format("Merchant with id %s not found", merchantId));

        // TODO : further validations
        // is the merchant free at this time?
        // is the customer and merchant's country and city the same?
        // do we have this customer's sufficient payment info to make an authorization?

        style.setRequestCount(style.getRequestCount() + 1);

        final StyleRequest styleRequest = new StyleRequest(style, merchant, customer, merchant.getAddress().getLocation(), PENDING, appointmentTime, new DateTime().plusMinutes(style.getDurationEstimate()));
        Long id = repository.allocateId(StyleRequest.class);
        styleRequest.setId(id);
        styleRequest.setPermanentId(id);

        // add payment authorization request to a new payments queue
        paymentService.holdPayment(styleRequest, customer); //fixme bind to braintree impl before deployment TLS v1.2 issues, GAE says it works in prod but not dev. Sounds idiotic

        repository.saveFew(styleRequest, style);

        emailTaskQueue.add(new PlacedStyleRequestNotification(styleRequest, merchant.getPreferences()));

//        Use feature toggle to turn this off or on instead of commenting
//        PushNotification pushNotification = new PushNotification()
//                .setAlert("New Style Request")
//                .setBadge(9)
//                .setSound("styleRequestSound.aiff")
//                .setDeviceTokens(customer.getDevice().getDeviceId()); // Nullable?
//        apnsQueue.add(new SendPushNotificationToApnsTask(pushNotification));

        logger.info("Placed Style Request. ID:'{}' customer:'{}' merchant:'{}'", styleRequest.getId(), customer.getEmail(), merchant.getEmail());
        return styleRequest;
    }

    @Override
    public void updateStyleRequest(StyleRequest styleRequest) {
        repository.saveOne(styleRequest);
    }

    @Override
    public void acceptStyleRequest(Long styleRequestId, Preferences preferences) {
        Assert.validId(styleRequestId);

        StyleRequest styleRequest = stateMgr.transition(styleRequestId, ACCEPTED);

        Merchant merchant = styleRequest.getMerchant();
        Assert.isTrue(!merchantService.isBooked(merchant.getId(), styleRequest.getAppointmentStartTime(),
                styleRequest.getAppointmentStartTime().plusMinutes(styleRequest.getStyle().getDurationEstimate())), "%s has an active booking during this period.", merchant.getFirstName());

        styleRequest.setAcceptedTime(DateTime.now());
        updateStyleRequest(styleRequest);

        emailTaskQueue.add(new AcceptedStyleRequestNotification(styleRequest, preferences));
    }

    @Override
    public void completeStyleRequest(Long styleRequestId, Preferences preferences) {
        Assert.validId(styleRequestId);
        StyleRequest styleRequest = stateMgr.transition(styleRequestId, COMPLETED);
        styleRequest.setCompletedTime(DateTime.now());
        updateStyleRequest(styleRequest);

        emailTaskQueue.add(new CompletedStyleRequestNotification(styleRequest, preferences));
    }

    @Override
    public void cancelStyleRequest(Long styleRequestId, Preferences preferences) {
        Assert.validId(styleRequestId);
        StyleRequest styleRequest = stateMgr.transition(styleRequestId, CANCELLED);
        styleRequest.setCancelledTime(DateTime.now());
        updateStyleRequest(styleRequest);

        //TODO notify merchant
        emailTaskQueue.add(new CancelledStyleRequestNotification(styleRequest, preferences));

    }



}
