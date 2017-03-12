package com.hair.business.services.payment.braintree;

import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.customer.AbstractServicesTestBase;
import com.hair.business.services.payment.PaymentService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.inject.Provider;

/**
 *
 * Created by olukoredeaguda on 11/03/2017.
 */
public class BraintreePaymentServiceImplTest extends AbstractServicesTestBase {

    private BraintreePaymentHandler handler = Mockito.mock(BraintreePaymentHandler.class);
    private Repository repository;
    private Provider provider = Mockito.mock(Provider.class);
    private final PaymentService braintreePaymentService = new BraintreePaymentServiceImpl(provider, handler, repository);
    StyleRequest styleRequest; Customer customer; Style style;

    @Before
    public void setUp() {
        repository = injector.getInstance(Repository.class);
        customer = createCustomer();
        style = createStyle();
        Merchant merchant = createMerchant();
        repository.saveFew(customer, style, merchant);
        styleRequest = new StyleRequest(style, merchant, customer, customer.getAddress().getLocation(), StyleRequestState.PENDING, null, null);
    }

    @Test
    public void holdPayment() throws Exception {
        assertThat(braintreePaymentService.holdPayment(styleRequest, customer), notNullValue());
    }

    @Test
    public void deductPayment() throws Exception {
    }

    @Test
    public void computeTax() throws Exception {
    }

    @Test
    public void updatePayment() throws Exception {
    }

    @Test
    public void refund() throws Exception {
    }

}