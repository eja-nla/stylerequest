package com.hair.business.services.payment.braintree;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Customer;
import com.braintreegateway.Environment;
import com.braintreegateway.Transaction;
import com.hair.business.services.customer.AbstractServicesTestBase;
import com.x.y.EntityTestConstants;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Provider;

/**
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class BraintreePaymentHandlerImplTest extends AbstractServicesTestBase {
    private BraintreePaymentHandler braintreePaymentHandler ;

    @Before
    public void setUp() {
        BraintreeGateway bt = new BraintreeGateway(Environment.SANDBOX, "f3x9jjczmbg6gz9y", "q4vncn2hg48mrvgt", "2615a1825093d71eb3daf9d0dd17d9d4");
        Provider<BraintreeGateway> p = () -> bt;
        braintreePaymentHandler = new BraintreePaymentHandlerImpl(p);
    }

    @Ignore
    @Test
    public void createTransaction() throws Exception {
        Transaction transaction = braintreePaymentHandler.authorizeTransaction(4533L, "",9.3, false);
        assertThat(transaction, notNullValue());
    }

    @Ignore
    @Test
    public void generateClientToken() throws Exception {
        String token = braintreePaymentHandler.generateClientToken("test");

        assertThat(token, notNullValue());
    }

    @Ignore
    @Test
    public void addPaymentMethod() throws Exception {
        com.hair.business.beans.entity.Customer c = EntityTestConstants.createCustomer();
        c.setId(4533L);
        Customer customer = braintreePaymentHandler.addPaymentMethod(c.getId(), c.getPayment().getPaymentMethods().get(0).getPaymentMethod(), false);

        assertThat(customer, notNullValue());
    }

    @Ignore
    @Test
    public void getPaymentMethodNonce() throws Exception {
        String token = braintreePaymentHandler.fetchNonce("test");

        assertThat(token, notNullValue());
    }

}