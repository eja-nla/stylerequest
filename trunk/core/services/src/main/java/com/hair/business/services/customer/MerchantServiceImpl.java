package com.hair.business.services.customer;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;

import java.util.Collection;
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

    @Inject
    public MerchantServiceImpl(Repository repository) {
        this.repository = repository;
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

    public Customer findCustomer(Customer customer) {
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
    public Style findStyle(long id) {
        return null;
    }

    @Override
    public Collection<Style> findStyles(Collection<Long> ids) {
        return null;
    }

    @Override
    public Collection<Style> findStylesByDescription(String description) {
        return null;
    }

    @Override
    public Collection<StyleRequest> findUpcomingAppointments(String merchantId) {
        //return repository.findByMultipleQuery(StyleRequest.class, "state ==", StyleRequestState.ACCEPTED);
        return null;
    }

    @Override
    public void createMerchant(String name, String email, String phone, Device device, Location location) {
        Long permId = repository.allocateId(Merchant.class);
        Merchant merchant = new Merchant(name, 0, email, phone, device, location);
        merchant.setId(permId);
        merchant.setPermanentId(permId);
        updateMerchant(merchant);
    }

    @Override
    public void updateMerchant(Merchant merchant) {
        repository.saveOne(merchant);
    }

    @Override
    public void updateRequest(StyleRequest styleRequest) {
        repository.saveOne(styleRequest);

    }

    @Override
    public void publishStyle(Style style, Merchant merchant) {

    }

    @Override
    public void cancelStyleRequest(Customer customer, StyleRequest request) {
        request.setState(StyleRequestState.CANCELLED);
        // send notification to customer
    }

    @Override
    public void pay(Customer customer, Merchant merchant) {

    }


}
