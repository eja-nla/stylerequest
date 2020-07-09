package com.hair.business.services.customer;

import static com.x.business.utilities.RatingUtil.averagingWeighted;
import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.constants.Preferences;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.payment.stripe.StripePaymentService;
import com.x.business.exception.DuplicateEntityException;
import com.x.business.scheduler.TaskQueue;
import com.x.business.scheduler.stereotype.ApnsTaskQueue;
import com.x.business.scheduler.stereotype.EmailTaskQueue;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Customer Service Impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = getLogger(CustomerServiceImpl.class);
    private final Repository repository;
    private final TaskQueue emailTaskQueue;
    private final TaskQueue apnsQueue;
    private final StripePaymentService stripe;
    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer with Id %s not found";

    @Inject
    CustomerServiceImpl(Repository repository, @EmailTaskQueue TaskQueue emailTaskQueue, @ApnsTaskQueue TaskQueue apnsQueue, StripePaymentService stripe) {
        this.repository = repository;
        this.emailTaskQueue = emailTaskQueue;
        this.apnsQueue = apnsQueue;
        this.stripe = stripe;
    }

    @Override
    public Customer findCustomer(Long id) {
        return repository.findOne(id, Customer.class);
    }

    @Override
    public Customer findCustomer(String email) {
        List<Customer> customers = repository.findByQuery(Customer.class, "email", email);
        if (customers.size() > 1) {
            throw new DuplicateEntityException("Expecting 1 entity but got " + customers.size());
        }

        return customers.get(0);
    }


    /**
     * Create a new customer
     * Payment profile will be created later with an explicit call to the payment profile creator API  by client.
     * */
    @Override
    public String createCustomer(Customer customer) {

        Assert.notNull(customer, "customer cannot be null");

        Long permId = repository.allocateId(Customer.class);
        customer.setId(permId);
        customer.setPermanentId(permId);

        saveCustomer(customer);

        logger.info("Created customer with ID {}", customer.getId());

        return permId.toString();
    }

    @Override
    public void saveCustomer(Customer customer) {
        Assert.notNull(customer);
        Assert.validId(customer.getId());

        repository.saveOne(customer);
    }

    @Override
    public Collection<StyleRequest> findStyleRequests(Long permanentId, StyleRequestState styleRequestState) throws IllegalArgumentException {
        Assert.validId(permanentId);
        Assert.notNull(styleRequestState, "Style request state cannot be null");
        List<String> conditions = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        conditions.add("customerPermanentId");
        conditions.add("state");
        values.add(permanentId);
        values.add(styleRequestState);
        return repository.findByQuery(StyleRequest.class, conditions, values);
    }

    @Override
    public void deactivateCustomer(Long customerId) {
        Assert.validId(customerId);

        Customer customer = repository.findOne(customerId, Customer.class);
        Assert.notNull(customer, String.format(CUSTOMER_NOT_FOUND_MESSAGE, customer));
        customer.setActive(false);

        repository.saveOne(customer);
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

    /**
     * if there is a valid appointment (up until when the appointment is completed),
     * a customer should be able to contact the merchant through us
     * */
    @Override
    public void contactMerchant(Long merchantId, String message) {

    }

    @Override
    public void updateRating(Long customerId, int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Review score must be between 1 and 5");
        }

        Assert.validId(customerId);
        Customer customer = repository.findOne(customerId, Customer.class);
        Assert.notNull(customer, String.format("Customer with id '%s' not found", customerId));
        final Map<Integer, Integer> weightedRatings = customer.getRatings();

        weightedRatings.put(score, weightedRatings.get(score) + 1);

        double weightedAverage = weightedRatings.entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue));
        customer.setScore(new BigDecimal(weightedAverage).setScale(1, RoundingMode.HALF_UP).doubleValue()); // Effective java item 48?

        repository.saveOne(customer);
    }

    @Override
    public Preferences updatePreferences(Long customerId, Preferences preferences) {
        Assert.notNull(preferences,"Preferences cannot be null");
        Assert.validId(customerId);

        Customer customer = repository.findOne(customerId, Customer.class);
        customer.setPreferences(preferences);

        repository.saveOne(customer);

        return preferences;
    }

}
