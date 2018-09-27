package com.hair.business.services.payment.braintree;

import static com.x.y.EntityTestConstants.createCustomer;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Transaction;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.customer.AbstractServicesTestBase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import javax.inject.Provider;

/**
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class BraintreePaymentServiceImplTest extends AbstractServicesTestBase {
    private BraintreePaymentService braintreePaymentService;

    @Before
    public void setUp() {
        BraintreeGateway bt = new BraintreeGateway(Environment.SANDBOX, "f3x9jjczmbg6gz9y", "q4vncn2hg48mrvgt", "2615a1825093d71eb3daf9d0dd17d9d4");
        Provider<BraintreeGateway> p = () -> bt;
        braintreePaymentService = new BraintreePaymentServiceImpl(p, Mockito.mock(Repository.class));
    }

    @Ignore
    @Test
    public void createTransaction() {
        Transaction transaction = braintreePaymentService.createTransaction("fake-valid-no-billing-address-nonce", 4533L,9.3, false);
        assertThat(transaction, notNullValue());
    }

    @Ignore
    @Test
    public void generateClientToken() {
        String token = braintreePaymentService.issueClientToken("4533");

        assertThat(token, notNullValue());
    }

    @Ignore
    @Test
    public void addPaymentMethod() {
        com.hair.business.beans.entity.Customer c = createCustomer();
        c.setId(4533L);

        boolean result = braintreePaymentService.addPaymentMethod("fake-valid-nonce", c.getId(), c.getPayment().getPaymentItems().get(0).getPaymentMethod(), false);

        assertThat(result, is(true));
    }


    //@Ignore
    @Test
    public void testCreateCustomer() {
        String id = braintreePaymentService.createCustomer(createCustomer(), "fake-valid-nonce");
        // see https://developers.braintreepayments.com/reference/general/testing/java for test nonces

        assertThat(id, notNullValue());
    }

}