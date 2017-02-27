package com.hair.business.services.merchant;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.StyleRequestService;
import com.x.business.notif.AcceptedStyleRequestNotification;
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
 * Customer Service Impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
public class MerchantServiceImpl implements MerchantService {

    static final Logger logger = Logger.getLogger(MerchantServiceImpl.class.getName());

    private final Repository repository;
    private final StyleRequestService styleRequestService;
    private final TaskQueue emailTaskQueue;
    private final TaskQueue apnsQueue;

    private final List<String> isBookedFilter = Arrays.asList("merchantPermanentId", "state", "appointmentStartTime <=", "appointmentStartTime >=");

    @Inject
    public MerchantServiceImpl(Repository repository, StyleRequestService styleRequestService, @EmailTaskQueue TaskQueue emailTaskQueue, @ApnsTaskQueue TaskQueue apnsQueue) {
        this.repository = repository;
        this.styleRequestService = styleRequestService;
        this.emailTaskQueue = emailTaskQueue;
        this.apnsQueue = apnsQueue;
    }

    @Override
    public Merchant findMerchant(long id) {
        return null;
    }

    @Override
    public Collection<Merchant> findMerchants(Collection<Long> ids) {
        return null;
    }

    @Override
    public Collection<Merchant> findMerchantsByDescription(String description) {
        return null;
    }

    @Override
    public Customer findCustomer(long id) {
        return null;
    }

    @Override
    public Collection<Customer> findCustomers(Collection<Long> ids) {
        return null;
    }

    @Override
    public Collection<Customer> findCustomersByDescription(String description) {
        return null;
    }

    @Override
    public void createMerchant(String firstName, String lastName, String displayName, String email, String phone, Device device, Location location) {
        Long permId = repository.allocateId(Merchant.class);
        Merchant merchant = new Merchant(firstName, lastName, displayName, email, phone, device, location);
        merchant.setId(permId);
        merchant.setPermanentId(permId);
        updateMerchant(merchant);
    }

    @Override
    public void updateMerchant(Merchant merchant) {
        Assert.notNull(merchant, "Merchant cannot be null.");
        repository.saveOne(merchant);
    }

    @Override
    public void pay(Customer customer, Merchant merchant) {

    }

    @Override
    public boolean isBooked(Long merchantId, DateTime period) {
        List<Object> isBookedValue = Arrays.asList(merchantId, StyleRequestState.ACCEPTED, period, period.minusMinutes(30));
        List<Long> foundRequests = repository.peekByQuery(StyleRequest.class, isBookedFilter, isBookedValue);

        return foundRequests.size() > 0;
    }

    @Override
    public void acceptStyleRequest(Long merchantId, Long styleRequestId) {
        Merchant merchant = repository.findOne(merchantId, Merchant.class);
        Assert.isFound(merchant, String.format("Merchant with id %s not found", merchantId));

        StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        Assert.isFound(styleRequest, String.format("StyleRequest with id %s not found", styleRequest));

        //Assert.isTrue(!isBooked(merchantId, styleRequest.getAppointmentStartTime()), "%s has an active booking during this period.", merchant.getFirstName());

        styleRequest.setState(StyleRequestState.ACCEPTED);
        styleRequestService.updateStyleRequest(styleRequest);

        emailTaskQueue.add(new AcceptedStyleRequestNotification(styleRequest, merchant.getPreferences()));

    }


}