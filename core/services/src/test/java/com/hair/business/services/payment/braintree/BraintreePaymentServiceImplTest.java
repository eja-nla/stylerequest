package com.hair.business.services.payment.braintree;

import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static com.x.y.EntityTestConstants.createStyleRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Transaction;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.customer.AbstractServicesTestBase;

import org.junit.Assert;
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
    Repository repository;

    private StyleRequest styleRequest = createStyleRequest();
    private Customer customer = createCustomer();
    private Merchant merchant = createMerchant();

    @Before
    public void setUp() {
        BraintreeGateway bt = new BraintreeGateway(Environment.SANDBOX, "f3x9jjczmbg6gz9y", "q4vncn2hg48mrvgt", "2615a1825093d71eb3daf9d0dd17d9d4");
        Provider<BraintreeGateway> p = () -> bt;
        braintreePaymentService = new BraintreePaymentServiceImpl(p, Mockito.mock(Repository.class));
        repository = injector.getInstance(Repository.class);
    }

    @Test
    public void testCreateTransaction() {
        double amount = 9.3;
        Transaction transaction = braintreePaymentService.createTransaction("fake-valid-no-billing-address-nonce", 4533L,amount, false);
        assertThat(transaction, notNullValue());
        assertThat(transaction.getAmount().doubleValue(), is(amount));
    }

    @Test
    public void testHoldPayment(){
        Style style = createStyle();
        style.setPrice(50.6);

        customer.setId(4533L);
        styleRequest.setStyle(style);
        styleRequest.setMerchant(merchant);
        repository.saveFew(style, styleRequest, customer, merchant);

        styleRequest = braintreePaymentService.holdPayment("fake-valid-nonce", styleRequest, customer);

        assertThat(styleRequest.getAuthorizedPayment().getPayment().getStatus(), is(Transaction.Status.AUTHORIZED));
    }

    @Test
    /*
    * ee https://developers.braintreepayments.com/reference/general/validation-errors/all/ruby#code-91522 for restrictions on settlement amount
    */
    public void testSettleTransaction() {
        double original = 9.00;
        double revised = original - 1.00;
        Transaction transaction = braintreePaymentService.createTransaction("fake-valid-no-billing-address-nonce", 4533L,original, false);
        assertThat(transaction.getStatus(), is(Transaction.Status.AUTHORIZED));

        transaction = braintreePaymentService.settleTransaction(transaction.getId(), revised);
        assertThat(transaction.getStatus(), is(Transaction.Status.SUBMITTED_FOR_SETTLEMENT));

        Assert.assertEquals(transaction.getAmount().doubleValue(), revised, 0.00);

    }

    @Test
    public void testIssueClientToken() {
        String token = braintreePaymentService.issueClientToken("4533");
        assertThat(token, notNullValue());
    }

    @Ignore
    @Test
    public void testAddPaymentMethod() {
        String id = "4533L";
        boolean result = braintreePaymentService.addPaymentMethod("fake-valid-nonce", id,
                new PaymentMethod("agreementId-" + id, false, id));

        assertThat(result, is(true));
    }


    @Test
    public void testCreateCustomer() {
        String id = braintreePaymentService.createCustomer(customer, "fake-valid-nonce");
        // see https://developers.braintreepayments.com/reference/general/testing/java for test nonces

        assertThat(id, notNullValue());
    }

}