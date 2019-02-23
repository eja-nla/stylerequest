package com.hair.business.entity;

import static com.com.hair.business.beans.utils.EntityTestUtils.TEST_UTILS;
import static com.x.y.EntityTestConstants.createCustomer;
import static com.x.y.EntityTestConstants.createMerchant;

import com.braintreegateway.Transaction;
import com.braintreegateway.util.SimpleNodeWrapper;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.repository.ObjectifyDatastoreRepositoryImpl;
import com.hair.business.dao.datastore.testbase.AbstractDatastoreTestBase;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Olukorede Aguda on 20/08/2016.
 *
 *
 */
public class StyleRequestPaymentTest extends AbstractDatastoreTestBase {

    private static final Logger LOGGER = Logger.getLogger(StyleRequestPaymentTest.class.getName());

    private Repository repo = new ObjectifyDatastoreRepositoryImpl();

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Ignore("No constructor found for com.braintreegateway.Transaction. We probably want to skip vendor objects anyway")
    @Test
    public void validateJsonFieldsMatchObjectFields() throws Exception {
        StyleRequestPayment pay1 = createPayment();

        LOGGER.info(TEST_UTILS.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pay1));
        String toJsonString = IOUtils.toString(new FileInputStream(new File("src/test/resources/stylerequestpayment.json")));
        StyleRequestPayment pay2 = TEST_UTILS.getObjectMapper().readValue(toJsonString, StyleRequestPayment.class);

        TEST_UTILS.validateFieldsAreEqual(pay1, pay2);
    }

    private StyleRequestPayment createPayment() throws IOException {
        Merchant merchant = createMerchant(); // we need to save these guys first so that objectify can successfully create the live Ref<?>
        Customer customer = createCustomer();
        repo.saveFew(merchant, customer);

        StyleRequestPayment p = new StyleRequestPayment(2432.545D, 435432L, 3254234L, true, PaymentType.PAYPAL);
        p.setId(new Random().nextLong());
        p.setPermanentId(new Random().nextLong());
        //Payment payment = createPayment("src/test/resources/paypalTestResources/payment.json", Payment.class);

        Transaction t = new Transaction(SimpleNodeWrapper.parse(transactionXml()));
        p.setPayment(t);
        p.setAuthorization(t);
        return p;
    }

    /**
     * Sample transaction response xml. Sample obtained from com.braintreegateway.util.Http.java@Line 133
     * */
    private String transactionXml(){
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<transaction>\n" +
                "  <id>some test id</id>\n" +
                "  <status>authorized</status>\n" +
                "  <type>sale</type>\n" +
                "  <currency-iso-code>EUR</currency-iso-code>\n" +
                "  <amount>9.30</amount>\n" +
                "  <merchant-account-id>testcompany</merchant-account-id>\n" +
                "  <sub-merchant-account-id nil=\"true\"/>\n" +
                "  <master-merchant-account-id nil=\"true\"/>\n" +
                "  <order-id nil=\"true\"/>\n" +
                "  <created-at type=\"datetime\">2017-03-08T23:09:07Z</created-at>\n" +
                "  <updated-at type=\"datetime\">2017-03-08T23:09:07Z</updated-at>\n" +
                "  <customer>\n" +
                "    <id>4533</id>\n" +
                "    <first-name>first</first-name>\n" +
                "    <last-name>customer</last-name>\n" +
                "    <company></company>\n" +
                "    <email>test@domain.com</email>\n" +
                "    <website></website>\n" +
                "    <phone></phone>\n" +
                "    <fax></fax>\n" +
                "  </customer>\n" +
                "  <billing>\n" +
                "    <id nil=\"true\"/>\n" +
                "    <first-name nil=\"true\"/>\n" +
                "    <last-name nil=\"true\"/>\n" +
                "    <company nil=\"true\"/>\n" +
                "    <street-address nil=\"true\"/>\n" +
                "    <extended-address nil=\"true\"/>\n" +
                "    <locality nil=\"true\"/>\n" +
                "    <region nil=\"true\"/>\n" +
                "    <postal-code nil=\"true\"/>\n" +
                "    <country-name nil=\"true\"/>\n" +
                "    <country-code-alpha2 nil=\"true\"/>\n" +
                "    <country-code-alpha3 nil=\"true\"/>\n" +
                "    <country-code-numeric nil=\"true\"/>\n" +
                "  </billing>\n" +
                "  <refund-id nil=\"true\"/>\n" +
                "  <refund-ids type=\"array\"/>\n" +
                "  <refunded-transaction-id nil=\"true\"/>\n" +
                "  <partial-settlement-transaction-ids type=\"array\"/>\n" +
                "  <authorized-transaction-id nil=\"true\"/>\n" +
                "  <settlement-batch-id nil=\"true\"/>\n" +
                "  <shipping>\n" +
                "    <id nil=\"true\"/>\n" +
                "    <first-name nil=\"true\"/>\n" +
                "    <last-name nil=\"true\"/>\n" +
                "    <company nil=\"true\"/>\n" +
                "    <street-address nil=\"true\"/>\n" +
                "    <extended-address nil=\"true\"/>\n" +
                "    <locality nil=\"true\"/>\n" +
                "    <region nil=\"true\"/>\n" +
                "    <postal-code nil=\"true\"/>\n" +
                "    <country-name nil=\"true\"/>\n" +
                "    <country-code-alpha2 nil=\"true\"/>\n" +
                "    <country-code-alpha3 nil=\"true\"/>\n" +
                "    <country-code-numeric nil=\"true\"/>\n" +
                "  </shipping>\n" +
                "  <custom-fields/>\n" +
                "  <avs-error-response-code nil=\"true\"/>\n" +
                "  <avs-postal-code-response-code>I</avs-postal-code-response-code>\n" +
                "  <avs-street-address-response-code>I</avs-street-address-response-code>\n" +
                "  <cvv-response-code>I</cvv-response-code>\n" +
                "  <gateway-rejection-reason nil=\"true\"/>\n" +
                "  <processor-authorization-code>0FXNVM</processor-authorization-code>\n" +
                "  <processor-response-code>1000</processor-response-code>\n" +
                "  <processor-response-text>Approved</processor-response-text>\n" +
                "  <additional-processor-response nil=\"true\"/>\n" +
                "  <voice-referral-number nil=\"true\"/>\n" +
                "  <purchase-order-number nil=\"true\"/>\n" +
                "  <tax-amount nil=\"true\"/>\n" +
                "  <tax-exempt type=\"boolean\">false</tax-exempt>\n" +
                "  <credit-card>\n" +
                "    <token>testToken1</token>\n" +
                "    <bin>411111</bin>\n" +
                "    <last-4>1111</last-4>\n" +
                "    <card-type>Visa</card-type>\n" +
                "    <expiration-month>12</expiration-month>\n" +
                "    <expiration-year>2018</expiration-year>\n" +
                "    <customer-location>US</customer-location>\n" +
                "    <cardholder-name>first customer</cardholder-name>\n" +
                "    <image-url>https://assets.braintreegateway.com/payment_method_logo/visa.png?environment=sandbox</image-url>\n" +
                "    <prepaid>Unknown</prepaid>\n" +
                "    <healthcare>Unknown</healthcare>\n" +
                "    <debit>Unknown</debit>\n" +
                "    <durbin-regulated>Unknown</durbin-regulated>\n" +
                "    <commercial>Unknown</commercial>\n" +
                "    <payroll>Unknown</payroll>\n" +
                "    <issuing-bank>Unknown</issuing-bank>\n" +
                "    <country-of-issuance>Unknown</country-of-issuance>\n" +
                "    <product-id>Unknown</product-id>\n" +
                "    <unique-number-identifier>edcc131c5a77e241d30a9ae976e4df10</unique-number-identifier>\n" +
                "    <venmo-sdk type=\"boolean\">false</venmo-sdk>\n" +
                "  </credit-card>\n" +
                "  <status-history type=\"array\">\n" +
                "    <status-event>\n" +
                "      <timestamp type=\"datetime\">2017-03-08T23:09:07Z</timestamp>\n" +
                "      <status>authorized</status>\n" +
                "      <amount>9.30</amount>\n" +
                "      <user>koredyte@yahoo.co.uk</user>\n" +
                "      <transaction-source>api</transaction-source>\n" +
                "    </status-event>\n" +
                "  </status-history>\n" +
                "  <plan-id nil=\"true\"/>\n" +
                "  <subscription-id nil=\"true\"/>\n" +
                "  <subscription>\n" +
                "    <billing-period-end-date nil=\"true\"/>\n" +
                "    <billing-period-start-date nil=\"true\"/>\n" +
                "  </subscription>\n" +
                "  <add-ons type=\"array\"/>\n" +
                "  <discounts type=\"array\"/>\n" +
                "  <descriptor>\n" +
                "    <name nil=\"true\"/>\n" +
                "    <phone nil=\"true\"/>\n" +
                "    <url nil=\"true\"/>\n" +
                "  </descriptor>\n" +
                "  <recurring type=\"boolean\">false</recurring>\n" +
                "  <channel nil=\"true\"/>\n" +
                "  <service-fee-amount nil=\"true\"/>\n" +
                "  <escrow-status nil=\"true\"/>\n" +
                "  <disbursement-details>\n" +
                "    <disbursement-date nil=\"true\"/>\n" +
                "    <settlement-amount nil=\"true\"/>\n" +
                "    <settlement-currency-iso-code nil=\"true\"/>\n" +
                "    <settlement-currency-exchange-rate nil=\"true\"/>\n" +
                "    <funds-held nil=\"true\"/>\n" +
                "    <success nil=\"true\"/>\n" +
                "  </disbursement-details>\n" +
                "  <disputes type=\"array\"/>\n" +
                "  <payment-instrument-type>credit_card</payment-instrument-type>\n" +
                "  <processor-settlement-response-code></processor-settlement-response-code>\n" +
                "  <processor-settlement-response-text></processor-settlement-response-text>\n" +
                "  <three-d-secure-info nil=\"true\"/>\n" +
                "</transaction>";
    }

}