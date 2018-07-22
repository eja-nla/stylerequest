package com.hair.business.services.merchant;

import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Address;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.StyleRequestService;
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
 * Customer Service Impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
public class MerchantServiceImpl implements MerchantService {

    private static final Logger logger = getLogger(MerchantServiceImpl.class);

    private final Repository repository;
    private final StyleRequestService styleRequestService;
    private final TaskQueue emailTaskQueue;
    private final TaskQueue apnsQueue;

    private final List<String> IS_BOOOKED_FILTER = Arrays.asList("merchantPermanentId", "state", "appointmentStartTime >=", "appointmentStartTime <=");

    @Inject
    public MerchantServiceImpl(Repository repository, StyleRequestService styleRequestService, @EmailTaskQueue TaskQueue emailTaskQueue, @ApnsTaskQueue TaskQueue apnsQueue) {
        this.repository = repository;
        this.styleRequestService = styleRequestService;
        this.emailTaskQueue = emailTaskQueue;
        this.apnsQueue = apnsQueue;
    }

    @Override
    public Merchant findMerchant(long id) {
        Assert.validId(id);

        return repository.findOne(id, Merchant.class);
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
    public void createMerchant(String firstName, String lastName, String displayName, String email, String phone, Device device, Address address) {
        Long permId = repository.allocateId(Merchant.class);
        Merchant merchant = new Merchant(firstName, lastName, displayName, email, phone, device, address);
        merchant.setId(permId);
        merchant.setPermanentId(permId);
        updateMerchant(merchant);
    }

    @Override
    public void updateMerchant(Merchant merchant) {
        repository.update(merchant);
    }

    @Override
    public void pay(Customer customer, Merchant merchant) {

    }

    @Override
    public boolean isBooked(Long merchantId, DateTime startTime, DateTime endTime) {
        List<Object> isBookedValue = Arrays.asList(merchantId, StyleRequestState.ACCEPTED, startTime, endTime);
        List<Long> foundRequests = repository.peekByQuery(StyleRequest.class, IS_BOOOKED_FILTER, isBookedValue);

        return !foundRequests.isEmpty();
    }

}
