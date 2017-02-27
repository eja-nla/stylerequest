package com.hair.business.services.payment.paypal;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

/**
 *
 *
 * Created by Olukorede Aguda on 24/02/2017.
 */
public class PaypalPaymentProcessorImpl implements PaypalPaymentProcessor {

    private static final Logger logger = Logger.getLogger(PaypalPaymentProcessorImpl.class.getName());
    private final APIContext paypalApiContext;
    private final Repository repository;
    private final PaymentRequestHandler paypalPaymentRequestHandler;
    private static final String CURRENCY = "USD";

    @Inject
    PaypalPaymentProcessorImpl(APIContext paypalApiContext, Repository repository, PaymentRequestHandler paypalPaymentRequestHandler) {
        this.paypalApiContext = paypalApiContext;
        this.repository = repository;
        this.paypalPaymentRequestHandler = paypalPaymentRequestHandler;
    }


    @Override
    public StyleRequestPayment authorizePayment(StyleRequest styleRequest, Customer customer, double tax, double total) {

        StyleRequestPayment styleRequestPayment = null;
        try {
            Authorization authorization = getAuthorization(paypalApiContext, customer, tax, total);

            styleRequestPayment = new StyleRequestPayment();
            //styleRequestPayment.setAuthorization(authorization);

        } catch (PayPalRESTException e) {
            logger.info(String.format("Could not authorize payment for stylerequest %s, reason %s", styleRequest.getId(), e.getMessage()));
        }
        repository.saveOne(styleRequestPayment);

        return styleRequestPayment;
    }

    @Override
    public StyleRequestPayment capturePreauthorizedPayment(String authorizationId, double totalAmount, boolean isFinalCapture) {

        Capture responseCapture;

        try {

            // ### Api Context
            // Pass in a `ApiContext` object to authenticate
            // the call and to send a unique request id
            // (that ensures idempotency). The SDK generates
            // a request id if you do not pass one explicitly.
            //APIContext paypalApiContext = new APIContext(clientID, clientSecret, mode);

            // ###Authorization
            // Retrieve a Authorization object
            // by making a Payment with intent
            // as 'authorize'
            final Authorization authorization = paypalPaymentRequestHandler.issueAuthorizationRequest(paypalApiContext, authorizationId);

            final Amount amount = createAmount(totalAmount, null);

            final Capture capture = createCapture(amount, isFinalCapture);

            // Capture by POSTing to
            // URI v1/payments/authorization/{authorization_id}/capture
            responseCapture = paypalPaymentRequestHandler.issueCaptureRequest(authorization, paypalApiContext, capture);

            logger.info("Capture id = " + responseCapture.getId() + " and status = " + responseCapture.getState());
            //ResultPrinter.addResult(req, resp, "Authorization Capture", Authorization.getLastRequest(), Authorization.getLastResponse(), null);
        } catch (PayPalRESTException e) {
            //ResultPrinter.addResult(req, resp, "Authorization Capture", Authorization.getLastRequest(), null, e.getMessage());
        }
        //req.getRequestDispatcher("response.jsp").forward(req, resp);

        final StyleRequestPayment styleRequestPayment = new StyleRequestPayment();
        //styleRequestPayment.setCapture(responseCapture);
        repository.saveOne(styleRequestPayment);

        return styleRequestPayment;
    }

    private Authorization getAuthorization(APIContext apiContext, Customer customer, double tax, double total) throws PayPalRESTException {

        Details details = createDetails(total, tax);


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

        // ###CreditCard
        // A resource representing a credit card that can be
        // used to fund a payment.
//        if (customer.getCreditCards() != null && customer.getCreditCards().size() > 0) {
//            CreditCard creditCard = customer.getCreditCards().get(0);
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
        // ###CreditCard
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

    /**
     * Fires an authorization request to Paypal
     */
    @Override
    public StyleRequestPayment holdPayment(StyleRequest styleRequest, Customer customer, double tax, double total) {
        return authorizePayment(styleRequest, customer, tax, total);
    }

    /**
     * Captures a pre-authorized Paypal payment
     */
    @Override
    public StyleRequestPayment releasePayment(String authorizationId, double totalAmount, boolean isFinalCapture) {
        return capturePreauthorizedPayment(authorizationId, totalAmount, isFinalCapture);
    }
}


