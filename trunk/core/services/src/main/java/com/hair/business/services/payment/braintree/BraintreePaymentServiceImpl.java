package com.hair.business.services.payment.braintree;

import static org.slf4j.LoggerFactory.getLogger;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Transaction;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentItem;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.beans.helper.PaymentStatus;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.payment.PaymentService;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Braintree payment processor
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class BraintreePaymentServiceImpl implements PaymentService {

    private static final Logger logger = getLogger(BraintreePaymentServiceImpl.class);
    private final BraintreeGateway gateway;
    private final BraintreePaymentHandler braintreePaymentHandler;
    private final Repository repository;

    @Inject
    public BraintreePaymentServiceImpl(Provider<BraintreeGateway> gatewayProvider, BraintreePaymentHandler braintreePaymentHandler, Repository repository) {
        this.gateway = gatewayProvider.get();
        this.braintreePaymentHandler = braintreePaymentHandler;
        this.repository = repository;
    }

    @Override
    public StyleRequest holdPayment(final StyleRequest styleRequest, final Customer customer) {
        Assert.notNull(styleRequest, styleRequest.getStyle());
        double price = styleRequest.getStyle().getPrice();
        Transaction result = braintreePaymentHandler.authorizeTransaction(customer.getId(), customer.getPayment().getDefaultPaymentMethod().getToken(), price, false);

        StyleRequestPayment authorizedPayment = new StyleRequestPayment(price, customer.getId(), styleRequest.getMerchant().getId(), false, result);
        styleRequest.setAuthorizedPayment(authorizedPayment);
        logger.debug("Successfully authorized payment for stylerequest " + styleRequest.getId());

        return styleRequest;
    }

    @Override
    public StyleRequest deductPreAuthPayment(Long styleRequestId, double totalAmount) {
        Assert.validId(styleRequestId);
        StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        Assert.notNull(styleRequest);
        Assert.notNull(styleRequest.getSettledPayment());
        String authorizedId = styleRequest.getSettledPayment().getAuthorization().getAuthorizedTransactionId();

        Transaction transaction = braintreePaymentHandler.settleTransaction(authorizedId, totalAmount);

        StyleRequestPayment settledPayment = new StyleRequestPayment();
        settledPayment.setSettled(true);
        settledPayment.setPaymentStatus(PaymentStatus.SETTLED);
        settledPayment.setPayment(transaction);

        styleRequest.setSettledPayment(settledPayment);

        repository.saveOne(styleRequest);

        return styleRequest;
    }

    @Override
    public void deductNonPreAuthPayment(String paymentToken, List<AddOn> items) {

        Transaction transaction = braintreePaymentHandler.settleTransaction(paymentToken, items);

        StyleRequestPayment settledPayment = new StyleRequestPayment();
        settledPayment.setSettled(true);
        settledPayment.setPaymentStatus(PaymentStatus.SETTLED);
        settledPayment.setPayment(transaction);
        //fixme store this somehow?
//        styleRequest.setSettledPayment(settledPayment);
//
//        repository.saveOne(styleRequest);
//
//        return styleRequest;
    }

    @Override
    public double computeTax(String countryCode, double itemPrice) {
        return 0;
    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault) {
        Customer customer = repository.findOne(customerId, Customer.class);
        customer.getPayment().getPaymentItems().add(new PaymentItem(paymentType, paymentMethod, isDefault));
        braintreePaymentHandler.addPaymentMethod(customerId, paymentMethod, isDefault);

        repository.saveOne(customer);
    }

    @Override
    public void refund(StyleRequest styleRequest) {

        Assert.notNull(styleRequest);
        Assert.notNull(styleRequest.getSettledPayment());

        Transaction settledTransaction = styleRequest.getSettledPayment().getPayment();
        Assert.notNull(settledTransaction);

        String transactionId = settledTransaction.getId();

        braintreePaymentHandler.refund(transactionId);
    }

}
