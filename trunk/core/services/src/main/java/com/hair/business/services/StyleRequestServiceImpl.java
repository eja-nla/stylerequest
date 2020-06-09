package com.hair.business.services;

import static com.hair.business.beans.constants.StyleRequestState.ACCEPTED;
import static com.hair.business.beans.constants.StyleRequestState.CANCELLED;
import static com.hair.business.beans.constants.StyleRequestState.COMPLETED;
import static com.hair.business.beans.constants.StyleRequestState.PENDING;
import static com.x.business.utilities.MessageConstants.CUSTOMER_NOT_FOUND;
import static com.x.business.utilities.MessageConstants.HAS_ACTIVE_BOOKING;
import static com.x.business.utilities.MessageConstants.MERCHANT_NOT_FOUND;
import static com.x.business.utilities.MessageConstants.NEW_STYLE_REQUEST;
import static com.x.business.utilities.MessageConstants.PLACED_STYLE_REQUEST;
import static com.x.business.utilities.MessageConstants.STYLE_NOT_FOUND;
import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.merchant.MerchantService;
import com.hair.business.services.payment.PaymentService;
import com.hair.business.services.pushNotification.PushNotificationServiceInternal;
import com.hair.business.services.state.StylerequestStateMgr;
import com.x.business.notif.AcceptedStyleRequestNotification;
import com.x.business.notif.CancelledStyleRequestNotification;
import com.x.business.notif.CompletedStyleRequestNotification;
import com.x.business.notif.PlacedStyleRequestNotification;
import com.x.business.scheduler.TaskQueue;
import com.x.business.scheduler.stereotype.EmailTaskQueue;
import com.x.business.utilities.Assert;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

/**
 * Style Request Service impl
 *
 * Created by Olukorede Aguda on 07/01/2017.
 */
public class StyleRequestServiceImpl extends AppointmentFinderExt implements StyleRequestService {

    private final Repository repository;
    private final TaskQueue emailTaskQueue;

    private final PaymentService paymentService;
    private final MerchantService merchantService;
    private final StylerequestStateMgr stateMgr;
    private final PushNotificationServiceInternal pushNotificationService;

    private final AtomicLong styleRequestCounter = new AtomicLong();

    private static final Logger logger = getLogger(StyleRequestServiceImpl.class);

    @Inject
    StyleRequestServiceImpl(Repository repository,
                            @EmailTaskQueue TaskQueue emailTaskQueue,
                            PaymentService paymentService, MerchantService merchantService, StylerequestStateMgr stateMgr, PushNotificationServiceInternal pushNotificationService) {
        super(repository);
        this.repository = repository;
        this.emailTaskQueue = emailTaskQueue;

        this.paymentService = paymentService;
        this.merchantService = merchantService;
        this.stateMgr = stateMgr;
        this.pushNotificationService = pushNotificationService;
    }

    @Override
    public StyleRequest findStyleRequest(Long id) {
        Assert.validId(id);

        return repository.findOne(id, StyleRequest.class);
    }

    /**
     *  Places a style request
     *  Goal is to keep this as simple as possible and move most validations upstream to the client
     * */
    @Override
    public final StyleRequest placeStyleRequest(String authorizationToken, Long styleId, Long customerId, Long merchantId, DateTime appointmentTime) {
        Assert.validIds(styleId, customerId, merchantId);
        Assert.dateInFuture(appointmentTime);

        final Style style = repository.findOne(styleId, Style.class);
        Assert.notNull(style, String.format(STYLE_NOT_FOUND, styleId));
        final Customer customer = repository.findOne(customerId, Customer.class);
        Assert.notNull(customer, String.format(CUSTOMER_NOT_FOUND, customerId));
        Assert.notNull(customer.getPaymentId(), "Payment method not found for this customer");
        final Merchant merchant = repository.findOne(merchantId, Merchant.class);
        Assert.notNull(merchant, String.format(MERCHANT_NOT_FOUND, merchantId));

        // TODO : further validations
        // is the merchant free at this time?
        // is the customer and merchant's country and city the same? Na, we should warn client if > 5 miles.
        // do we have this customer's sufficient payment info to make an authorization?
        //- on rethink We really do not want to do this here. We should move to the client

        styleRequestCounter.set(style.getRequestCount());
        style.setRequestCount(styleRequestCounter.incrementAndGet());

        final StyleRequest styleRequest = new StyleRequest(style, merchant, customer, merchant.getAddress().getLocation(), PENDING, appointmentTime, new DateTime().plusMinutes(style.getDurationEstimate()));
        final Long id = repository.allocateId(StyleRequest.class);
        styleRequest.setId(id);
        styleRequest.setPermanentId(id);

        paymentService.authorize(authorizationToken, styleRequest);

        emailTaskQueue.add(new PlacedStyleRequestNotification(styleRequest, merchant.getPreferences()));
        pushNotificationService.scheduleSend(customer.getDevice().getDeviceId(), NEW_STYLE_REQUEST);

        repository.saveFew(style, styleRequest);

        logger.info(PLACED_STYLE_REQUEST, styleRequest.getId(), customer.getEmail(), merchant.getEmail());

        return styleRequest;
    }

    @Override
    public void updateStyleRequest(StyleRequest styleRequest) {
        repository.saveOne(styleRequest);
    }

    @Override
    public void acceptStyleRequest(Long styleRequestId, Preferences preferences) {
        Assert.validId(styleRequestId);

        final StyleRequest styleRequest = stateMgr.transition(styleRequestId, ACCEPTED);

        final Merchant merchant = styleRequest.getMerchant();
        Assert.isTrue(!merchantService.isBooked(merchant.getId(), styleRequest.getAppointmentStartTime(),
                styleRequest.getAppointmentStartTime().plusMinutes(styleRequest.getStyle().getDurationEstimate())), HAS_ACTIVE_BOOKING, merchant.getFirstName());

        styleRequest.setAcceptedTime(DateTime.now());
        updateStyleRequest(styleRequest);

        emailTaskQueue.add(new AcceptedStyleRequestNotification(styleRequest));
    }

    @Override
    public void completeStyleRequest(Long styleRequestId, Preferences preferences) {
        Assert.validId(styleRequestId);
        final StyleRequest styleRequest = stateMgr.transition(styleRequestId, COMPLETED);

        paymentService.settlePreAuthPayment(styleRequest);
        styleRequest.setCompletedTime(DateTime.now());
        updateStyleRequest(styleRequest);

        emailTaskQueue.add(new CompletedStyleRequestNotification(styleRequest, preferences));
    }

    @Override
    public void cancelStyleRequest(Long styleRequestId, Preferences preferences) {
        Assert.validId(styleRequestId);
        final StyleRequest styleRequest = stateMgr.transition(styleRequestId, CANCELLED);
        styleRequest.setCancelledTime(DateTime.now());
        updateStyleRequest(styleRequest);

        //TODO notify merchant
        emailTaskQueue.add(new CancelledStyleRequestNotification(styleRequest, preferences));

    }
}
