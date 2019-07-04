package com.hair.business.services.payment.braintree;

import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static com.x.y.EntityTestConstants.createStyleRequest;
import static com.x.y.EntityTestConstants.createTaxInfo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenGateway;
import com.braintreegateway.CustomerGateway;
import com.braintreegateway.PaymentMethodGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionGateway;
import com.braintreegateway.test.Nonce;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.beans.helper.PaymentStatus;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.customer.AbstractServicesTestBase;
import com.hair.business.services.tax.SalesTaxPalHttpClientImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;

import javax.inject.Provider;

/**
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class BraintreePaymentServiceImplTest extends AbstractServicesTestBase {
    //BraintreeGateway braintreeGateway = new BraintreeGateway(Environment.SANDBOX, "f3x9jjczmbg6gz9y", "q4vncn2hg48mrvgt", "2615a1825093d71eb3daf9d0dd17d9d4");
    Repository repository;

    private StyleRequest styleRequest = createStyleRequest();
    private Customer customer = createCustomer();
    private Merchant merchant = createMerchant();

    private StyleRequest result = mock(StyleRequest.class);
    private StyleRequestPayment srp;
    private Transaction t = mock(Transaction.class);
    private BraintreeGateway braintreeGateway = mock(BraintreeGateway.class);
    private Result braintreeResult = mock(Result.class);
    private TransactionGateway txg = mock(TransactionGateway.class);

    private BraintreePaymentService braintreePaymentService;
    @Before
    public void setUp() throws IOException {

        when(braintreeResult.isSuccess()).thenReturn(true);
        when(braintreeResult.getTarget()).thenReturn(t);
        when(txg.sale(Matchers.any())).thenReturn(braintreeResult);
        when(txg.submitForSettlement(any(), any(BigDecimal.class))).thenReturn(braintreeResult);

        when(braintreeGateway.transaction()).thenReturn(txg);

        PaymentMethodGateway pmg = Mockito.mock(PaymentMethodGateway.class);
        when(pmg.create(any())).thenReturn(braintreeResult);
        when(braintreeGateway.paymentMethod()).thenReturn(pmg);

        Provider<BraintreeGateway> p = () -> braintreeGateway;

        repository = injector.getInstance(Repository.class);

        when(t.getStatus()).thenReturn(Transaction.Status.AUTHORIZED);
        when(t.getAmount()).thenReturn(new BigDecimal(55.0));
        com.braintreegateway.Customer bc = mock(com.braintreegateway.Customer.class);
        when(bc.getId()).thenReturn(Long.toString(customer.getId()));
        when(t.getCustomer()).thenReturn(bc);
        when(t.getTaxAmount()).thenReturn(new BigDecimal(.5));
        srp = createPayment(t);
        when(result.getAuthorizedPayment()).thenReturn(srp);

        SalesTaxPalHttpClientImpl salesTaxPalHttpClient = mock(SalesTaxPalHttpClientImpl.class);
//        when(salesTaxPalHttpClient.getBaseUrl()).thenReturn("http://testbaseurl");
        when(salesTaxPalHttpClient.doGet(any(), anyString())).thenReturn(createTaxInfo());
        when(salesTaxPalHttpClient.doPost(any(), any(), anyString())).thenReturn(createTaxInfo());
        braintreePaymentService = new BraintreePaymentServiceImpl(p, salesTaxPalHttpClient, repository);
    }

    @Test
    public void testCreateTransaction() {
        double amount = 9.3;
        when(t.getAmount()).thenReturn(new BigDecimal(amount));

        Transaction transaction = braintreePaymentService.createTransaction(Nonce.Transactable,  "2","CUSTOMER ID",amount, .9, false);
        assertThat(transaction, notNullValue());
        assertThat(transaction.getAmount().doubleValue(), is(amount));
    }

    @Test
    public void testAuthorize(){
        Style style = createStyle();
        style.setPrice(50.6);

        customer.setId(4533L);
        customer.setPaymentId("testID");
        styleRequest.setStyle(style);
        styleRequest.setMerchant(merchant);
        styleRequest.setCustomer(customer);

        repository.saveFew(style, styleRequest, customer, merchant);

        styleRequest = braintreePaymentService.authorize(Nonce.Transactable, styleRequest);

        assertThat(styleRequest.getAuthorizedPayment().getPaymentStatus(), is(PaymentStatus.AUTHORIZED));
    }

    @Test
    /*
    * ee https://developers.braintreepayments.com/reference/general/validation-errors/all/ruby#code-91522 for restrictions on settlement amount
    */
    public void testSettleTransaction() {
        double original = 9.00;
        double revised = original - 1.00;
        Transaction transaction = braintreePaymentService.createTransaction(Nonce.Transactable, "2","CUST ID",original, .9, false);
        assertThat(transaction.getStatus(), is(Transaction.Status.AUTHORIZED));

        when(t.getStatus()).thenReturn(Transaction.Status.SUBMITTED_FOR_SETTLEMENT);
        transaction = braintreePaymentService.settleTransaction(transaction.getId(), revised);
        assertThat(transaction.getStatus(), is(Transaction.Status.SUBMITTED_FOR_SETTLEMENT));

        when(t.getAmount()).thenReturn(new BigDecimal(revised));
        Assert.assertEquals(transaction.getAmount().doubleValue(), revised, 0.00);

    }

    @Test
    public void testIssueClientToken() {
        ClientTokenGateway ctg = mock(ClientTokenGateway.class);
        when(ctg.generate(any())).thenReturn("RESULT_SENTINEL");
        when(braintreeGateway.clientToken()).thenReturn(ctg);
        String token = braintreePaymentService.issueClientToken("4533");
        assertThat(token, notNullValue());
    }

    @Test
    public void testAddPaymentMethod() {
        String id = "4533L";
        boolean result = braintreePaymentService.addPaymentMethod(Nonce.Transactable, new PaymentMethod("agreementId", false, id));

        assertThat(result, is(true));
    }


    @Test
    public void testCreateCustomer() {
        CustomerGateway cgw = Mockito.mock(CustomerGateway.class);
        when(braintreeGateway.customer()).thenReturn(cgw);

        Result result = mock(Result.class);
        when(result.isSuccess()).thenReturn(true);
        when(cgw.create(any())).thenReturn(result);

        com.braintreegateway.Customer btc = mock(com.braintreegateway.Customer.class);
        when(btc.getId()).thenReturn("2");

        when(result.getTarget()).thenReturn(btc);

        String id = braintreePaymentService.createProfile(Long.toString(customer.getId()), Nonce.Transactable);
        // see https://developers.braintreepayments.com/reference/general/testing/java for test nonces

        assertThat(id, notNullValue());
    }

    @Test
    public void testRefundPart() {
        String id = "4533L";
        when(braintreeGateway.transaction()).thenReturn(txg);
        when(txg.refund(id, BigDecimal.ONE)).thenReturn(braintreeResult);
        assertThat(braintreePaymentService.refund(id, BigDecimal.ONE).isSuccess(), is(true));
    }

    @Test
    public void testRefundFull() {
        String id = "4533L";
        when(braintreeGateway.transaction()).thenReturn(txg);
        when(txg.refund(id)).thenReturn(braintreeResult);
        assertThat(braintreePaymentService.refund(id, BigDecimal.ZERO).isSuccess(), is(true));
    }

    @Test
    public void testComputeTax(){

    }

    private StyleRequestPayment createPayment(Transaction transaction){
        final StyleRequestPayment settledPayment = new StyleRequestPayment(
                transaction.getAmount().doubleValue(),
                Long.valueOf(transaction.getCustomer().getId()),
                1L,
                false
        );
        settledPayment.setSettled(true);
        settledPayment.setPaymentStatus(PaymentStatus.AUTHORIZED);
        settledPayment.setTransactionId(transaction.getId());

        return settledPayment;

    }



}