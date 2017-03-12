package com.hair.business.services.payment.braintree;

import static org.slf4j.LoggerFactory.getLogger;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Transaction;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentItem;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.payment.PaymentService;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Braintree payment processor
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class BraintreePaymentServiceImpl implements PaymentService {

    private final Logger logger = getLogger(this.getClass());
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
    public StyleRequestPayment holdPayment(StyleRequest styleRequest, Customer customer) {
        Assert.notNull(styleRequest, styleRequest.getStyle());
        String clientToken = braintreePaymentHandler.generateClientToken(customer.getId().toString());
        String nonce = braintreePaymentHandler.fetchNonce(clientToken);
        double price = styleRequest.getStyle().getPrice();
        Transaction result = braintreePaymentHandler.authorizeTransaction(customer.getId(), customer.getPayment().getDefaultPaymentMethod().getPaymentMethod().getToken(), price, false);

        logger.debug("Successfully authorized payment for stylerequest " + styleRequest.getId());
        return new StyleRequestPayment(price, customer.getId(), styleRequest.getMerchant().getId(), false, result);
    }

    @Override
    public StyleRequestPayment deductPayment(String authorizationId, double totalAmount, boolean isFinalCapture) {
        return null;
    }

    @Override
    public double computeTax(String countryCode, double itemPrice) {
        return 0;
    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault) {
        Customer customer = repository.findOne(customerId, Customer.class);
        customer.getPayment().getPaymentMethods().add(new PaymentItem(paymentType, paymentMethod, isDefault));
        braintreePaymentHandler.addPaymentMethod(customerId, paymentMethod, isDefault);

        repository.saveOne(customer);
    }

    @Override
    public StyleRequestPayment refund(StyleRequest styleRequest, Customer customer) {
        return null;
    }

}
