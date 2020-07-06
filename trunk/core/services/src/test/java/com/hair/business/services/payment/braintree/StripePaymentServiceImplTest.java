package com.hair.business.services.payment.braintree;

import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyleRequest;
import static com.x.y.EntityTestConstants.createTaxInfo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.customer.AbstractServicesTestBase;
import com.hair.business.services.tax.SalesTaxPalHttpClientImpl;

import org.junit.Before;

import java.io.IOException;

/**
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class StripePaymentServiceImplTest extends AbstractServicesTestBase {
    Repository repository;

    private StyleRequest styleRequest = createStyleRequest();
    private Customer customer = createCustomer();
    private Merchant merchant = createMerchant();

    private StyleRequest result = mock(StyleRequest.class);
    private StyleRequestPayment srp;

    private BraintreePaymentService braintreePaymentService;
    @Before
    public void setUp() throws IOException {


        //srp = createPayment(t);
        when(result.getAuthorizedPayment()).thenReturn(srp);

        SalesTaxPalHttpClientImpl salesTaxPalHttpClient = mock(SalesTaxPalHttpClientImpl.class);
//        when(salesTaxPalHttpClient.getBaseUrl()).thenReturn("http://testbaseurl");
        when(salesTaxPalHttpClient.doGet(any(), anyString())).thenReturn(createTaxInfo());
        when(salesTaxPalHttpClient.doPost(any())).thenReturn(createTaxInfo());
    }

//    @Test
//    void test(){
//        String id = service.createCustomer("intellijc1");
//        String intent = service.createPaymentIntent(2222, id);
//        service.cancelPayment(intent); // pending, so we can cancel

    //       service.chargeNow("cus_HaKp6AUpG8D4aL", 1200, "acct_1H0draCQiLLG2cvn", null);

    //      String intent = service.createPaymentIntent(2223, "cus_HaKp6AUpG8D4aL", null); // lets try to pay with a valid user and payment method

//    String res = service.authorize("cus_HaKp6AUpG8D4aL", 3000, "acct_1H0draCQiLLG2cvn","test service from intellij", null);
//        service.capture(3000, res, null);
//    }



}