package com.hair.business.services.payment.paypal;

import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.beans.helper.PaymentStatus;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Capture;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.x.business.utilities.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Paypal payment service implementation
 *
 * Created by Olukorede Aguda on 24/02/2017.
 */
public class PaypalPaymentServiceImpl implements PaypalPaymentService {

    private static final Logger logger = Logger.getLogger(PaypalPaymentServiceImpl.class.getName());
    private final APIContext paypalApiContext;
    private final Repository repository;
    private final PaymentRequestHandler paypalPaymentRequestHandler;
    private static final String CURRENCY = "USD";

    @Inject
    PaypalPaymentServiceImpl(APIContext paypalApiContext, Repository repository, PaymentRequestHandler paypalPaymentRequestHandler) {
        this.paypalApiContext = paypalApiContext;
        this.repository = repository;
        this.paypalPaymentRequestHandler = paypalPaymentRequestHandler;
    }


    @Override
    public StyleRequestPayment authorizePayment(StyleRequest styleRequest, Customer customer) {

        StyleRequestPayment styleRequestPayment = null;
        try {
            Authorization authorization = acquireAuthorization(styleRequest, customer);

            Assert.notNull(authorization, "Failed to obtain paypal authorization for stylerequest " + styleRequest.getId());

            styleRequestPayment = new StyleRequestPayment(); //todo set other fields
//            styleRequestPayment.setAuthorization((AuthorizationExt) authorization);
            styleRequestPayment.setPaymentStatus(PaymentStatus.AUTHORIZED);
            repository.saveOne(styleRequestPayment);
        } catch (PayPalRESTException e) {
            logger.info(String.format("Could not authorize payment for stylerequest %s, reason %s", styleRequest.getId(), e.getMessage()));
        }

        return styleRequestPayment;
    }

    @Override
    public StyleRequestPayment capturePreauthorizedPayment(String authorizationId, double totalAmount, boolean isFinalCapture) {

        Capture responseCapture;

        try {

            final Authorization authorization = paypalPaymentRequestHandler.fetchAuthorization(paypalApiContext, authorizationId);
            Assert.notNull(authorization, "Unable to fetch paypal authorization ID: " + authorizationId);

            final Amount amount = createAmount(totalAmount, null);

            final Capture capture = createCapture(amount, isFinalCapture);

            responseCapture = paypalPaymentRequestHandler.issueCaptureRequest(authorization.getId(), paypalApiContext, capture);

            logger.info("Capture id = " + responseCapture.getId() + " and status = " + responseCapture.getState());
        } catch (PayPalRESTException e) {
            logger.info(String.format("Could not capture payment for style request %s, reason %s", authorizationId, e.getMessage()));
        }

        final StyleRequestPayment styleRequestPayment = new StyleRequestPayment();
        //styleRequestPayment.setCapture((CaptureExt) responseCapture);
        styleRequestPayment.setPaymentStatus(PaymentStatus.SETTLED);
        repository.saveOne(styleRequestPayment);

        return styleRequestPayment;
    }

    private Authorization acquireAuthorization(StyleRequest styleRequest, Customer customer) throws PayPalRESTException {

        double total = styleRequest.getStyle().getPrice();
        double tax = computeTax(styleRequest.getMerchant().getAddress().getLocation().getCountryCode(), total);
        Details details = createDetails(tax, total);


        // ###Amount
        // Let's you specify a payment amount.
        Amount amount = createAmount(total, details);

        Transaction transaction = createTransaction(amount, "Payment for new style Patewo");

        // The Payment creation API requires a list of
        // Transaction; add the created `Transaction`
        // to a List
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // ###Address
        // Base Address object used as shipping or billing
        // address in a payment. [Optional]
        Address billingAddress = createBillingAddress(customer); // we don't need this

        // ###FundingInstrument
        // A resource representing a Payeer's funding instrument.
        // Use a Payer ID (A unique identifier of the payer generated
        // and provided by the facilitator. This is required when
        // creating or using a tokenized funding instrument)
        // and the `CreditCardDetails`
        FundingInstrument fundingInstrument = new FundingInstrument();

        // ###PaymentMethod
        // A resource representing a credit card that can be
        // used to fund a payment.
//        if (customer.getCreditCards() != null && customer.getCreditCards().size() > 0) {
//            PaymentMethod creditCard = customer.getCreditCards().get(0);
//            creditCard.setBillingAddress(billingAddress);
//            fundingInstrument.setCreditCard(creditCard);
//        }

        // The Payment creation API requires a list of
        // FundingInstrument; add the created `FundingInstrument`
        // to a List
        List<FundingInstrument> fundingInstruments = new ArrayList<>();
        fundingInstruments.add(fundingInstrument);

        // ###Payer
        // A resource representing a Payer that funds a payment
        // Use the List of `FundingInstrument` and the Payment Method
        // as 'credit_card'
        Payer payer = createPayer(fundingInstruments);

        // ###Payment
        // A Payment Resource; create one using
        // the above types and intent as 'authorize'
        Payment payment = createPayment(payer, transactions);

        Payment responsePayment = paypalPaymentRequestHandler.issuePaymentRequest(payment, paypalApiContext);

        // fixme before returning, find out if the auth was declined and retry using Customer's second credit card
        return responsePayment.getTransactions().get(0).getRelatedResources().get(0).getAuthorization();
    }

    @Override
    public StyleRequest deductPreAuthPayment(Long styleRequestId, double totalAmount) {
        return null;
    }

    @Override
    public void deductNonPreAuthPayment(String paymentToken, List<AddOn> addOns) {

    }

    @Override
    public Customer createCustomerPaymentProfile(Customer customer, PaymentType paymentType, boolean isDefault) {
        return null;
    }

    @Override
    public void refund(StyleRequest styleRequest) {

    }

    /**
     * Captures a pre-authorized Paypal payment
     */
    //@Override
    public StyleRequestPayment deductPayment(String authorizationId, double totalAmount, boolean isFinalCapture) {
        return capturePreauthorizedPayment(authorizationId, totalAmount, isFinalCapture);
    }

    @Override
    public double computeTax(String countryCode, double itemPrice) {
        return 0;
    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault) {

    }

//    @Override
    public StyleRequestPayment refund(StyleRequest styleRequest, Customer customer) {
        return null;
    }

    private Transaction createTransaction(Amount amount, String description) {
        // ###Transaction
        // A transaction defines the contract of a
        // payment - what is the payment for and who
        // is fulfilling it. Transaction is created with
        // a `Payee` and `Amount` types
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);

        return transaction;
    }

    /*
    * // ###Amount
    * // Let's you specify a capture amount.
    * */
    private Amount createAmount(double totalAmount, Details details) {
        Amount amount = new Amount();
        amount.setCurrency(CURRENCY);  // todo fix to use the CURRENCY of the merchant's country, we have this info.
        amount.setTotal(Double.toString(totalAmount));
        amount.setDetails(details);

        return amount;
    }
    private Capture createCapture(Amount amount, boolean isFinalCapture) {
        // ###Capture
        // A capture transaction
        Capture capture = new Capture();
        capture.setAmount(amount);

        // ##IsFinalCapture
        // If set to true, all remaining
        // funds held by the authorization
        // will be released in the funding
        // instrument. Default is �false�.
        capture.setIsFinalCapture(isFinalCapture);
        return capture;
    }
    private Details createDetails(double tax, double total) {
        // ###Details
        // Let's you specify details of a payment amount.
        Details details = new Details();
        details.setSubtotal(Double.toString(total));
        details.setTax(Double.toString(tax)); //don't think we need a details object in our use-case

        return details;
    }
    private Address createBillingAddress(Customer customer){
        // ###Address
        // Base Address object used as shipping or billing
        // address in a payment. [Optional]
        Address billingAddress = new Address();
        billingAddress.setCity(customer.getAddress().getLocation().getCity());
        billingAddress.setCountryCode(customer.getAddress().getLocation().getCountryCode());
        billingAddress.setLine1(customer.getAddress().getAddressLine());
        billingAddress.setPostalCode(customer.getAddress().getPostCode());
        billingAddress.setState(customer.getAddress().getLocation().getState());

        return billingAddress;
    }
    private CreditCard getCreditCard (Address billingAddress){
        // ###PaymentMethod
        // A resource representing a credit card that can be
        // used to fund a payment.
        CreditCard creditCard = new CreditCard();
        creditCard.setBillingAddress(billingAddress);
        creditCard.setCvv2("874");
        creditCard.setExpireMonth(11);
        creditCard.setExpireYear(2018);
        creditCard.setFirstName("Joe");
        creditCard.setLastName("Shopper");
        creditCard.setNumber("4417119669820331");
        creditCard.setType("visa");

        return creditCard;
    }
    private Payer createPayer(List<FundingInstrument> fundingInstruments) {
        // ###Payer
        // A resource representing a Payer that funds a payment
        // Use the List of `FundingInstrument` and the Payment Method
        // as 'credit_card'
        Payer payer = new Payer();
        payer.setFundingInstruments(fundingInstruments);
        payer.setPaymentMethod("credit_card");

        return payer;
    }
    private Payment createPayment(Payer payer, List<Transaction> transactions) {
        // ###Payment
        // A Payment Resource; create one using
        // the above types and intent as 'authorize'
        Payment payment = new Payment();
        payment.setIntent("authorize");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        return payment;
    }

    @Override
    public com.braintreegateway.Transaction createTransaction(String nonce, Long customerId, double amount, boolean isSettled) {
        return null;
    }

    @Override
    public com.braintreegateway.Transaction settleTransaction(String transactionId, double amount) {
        return null;
    }

    @Override
    public com.braintreegateway.Transaction settleTransaction(String nonce, String paymentMethodToken, List<AddOn> addOns) {
        return null;
    }

    @Override
    public boolean addPaymentMethod(String nonce, String customerId, PaymentMethod payment) {
        return false;
    }

    @Override
    public StyleRequest authorize(String nonce, Long styleRequestId, Long customerId) {
        return null;
    }

    @Override
    public StyleRequest deductPreAuthPayment(String nonce, Long styleRequestId, double totalAmount) {
        return null;
    }

    @Override
    public void deductNonPreAuthPayment(String nonce, String paymentToken, List<AddOn> items) {

    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, String nonce, boolean isDefault) {

    }

    @Override
    public Customer createCustomerPaymentProfile(Customer customer, PaymentType paymentType, String nonce, boolean isDefault) {
        return null;
    }

    @Override
    public String issueClientToken(String customerId) {
        return null;
    }

    @Override
    public String createCustomer(Customer customer, String nonce) {
        return null;
    }

    @Override
    public void refund(String transactionId) {

    }
}


