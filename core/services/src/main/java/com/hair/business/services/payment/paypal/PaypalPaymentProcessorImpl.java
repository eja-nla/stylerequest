package com.hair.business.services.payment.paypal;

import com.hair.business.beans.constants.PaymentType;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 * fixme revisit (Needs TLS 1.2.) - preferably when appengine support java 8
 */
public class PaypalPaymentProcessorImpl implements PaypalPaymentProcessor {

    private static final Logger logger = Logger.getLogger(PaypalPaymentProcessorImpl.class.getName());
    private final APIContext paypalApiContext;
    private final Repository repository;
    private Map<String, String> map = new HashMap<>();

    @Inject
    public PaypalPaymentProcessorImpl(APIContext paypalApiContext, Repository repository) {
        this.paypalApiContext = paypalApiContext;
        this.repository = repository;
    }

    @Override
    public Payment pay(Long payerId, Long recipientId, String guid) {
        return null;
    }

    @Override
    public void refund(String saleId, String currency, Double totalAmount) {

    }

    private Payment createPayment(Long payerId, Long recipientId) {
        Payment createdPayment = null;

        // ### Api Context
        // Pass in a `ApiContext` object to authenticate
        // the call and to send a unique request id
        // (that ensures idempotency). The SDK generates
        // a request id if you do not pass one explicitly.
//        if (payerId != null) {
//            Payment payment = new Payment();
//            if (guid != null) {
//                payment.setId(map.get(guid));
//            }
//
//            PaymentExecution paymentExecution = new PaymentExecution();
//            paymentExecution.setPayerId(payerId.toString());
//            try {
//
//                createdPayment = payment.execute(paypalApiContext, paymentExecution);
//                //ResultPrinter.addResult(payerId, resp, "Executed The Payment", Payment.getLastRequest(), Payment.getLastResponse(), null);
//            } catch (PayPalRESTException e) {
//                //ResultPrinter.addResult(payerId, resp, "Executed The Payment", Payment.getLastRequest(), null, e.getMessage());
//            }
//        } else {

            // ###Details
            // Let's you specify details of a payment amount.
            Details details = new Details();
            details.setShipping("1");
            details.setSubtotal("5");
            details.setTax("1");

            // ###Amount
            // Let's you specify a payment amount.
            Amount amount = new Amount();
            amount.setCurrency("USD");
            // Total must be equal to sum of shipping, tax and subtotal.
            amount.setTotal("7");
            amount.setDetails(details);

            // ###Transaction
            // A transaction defines the contract of a
            // payment - what is the payment for and who
            // is fulfilling it. Transaction is created with
            // a `Payee` and `Amount` types
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("This is the payment transaction description.");
            transaction.setInvoiceNumber(repository.allocateId(com.hair.business.beans.entity.Payment.class).toString());

            // ### Items
            Item item = new Item();
            item.setName("Ground Coffee 40 oz").setQuantity("1").setCurrency("USD").setPrice("5");
            ItemList itemList = new ItemList();
            List<Item> items = new ArrayList<>();
            items.add(item);
            itemList.setItems(items);

            transaction.setItemList(itemList);


            // The Payment creation API requires a list of
            // Transaction; add the created `Transaction`
            // to a List
            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            // ###Payer
            // A resource representing a Payer that funds a payment
            // Payment Method
            // as 'paypal'
            Payer payer = new Payer();
            payer.setPaymentMethod(PaymentType.PAYPAL.name());

            // ###Payment
            // A Payment Resource; create one using
            // the above types and intent as 'sale'
            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);

            // ###Redirect URLs
            RedirectUrls redirectUrls = new RedirectUrls();
            String guid = UUID.randomUUID().toString().replaceAll("-", "");
            redirectUrls.setCancelUrl("http" + "://" + "localhost" + ":" + "4657" + "/paypal" + "/paymentwithpaypal?guid=" + guid);
            redirectUrls.setReturnUrl("http" + "://" + "localhost" + ":" + "4657" + "/paypal" + "/paymentwithpaypal?guid=" + guid);
            payment.setRedirectUrls(redirectUrls);

            // Create a payment by posting to the APIService
            // using a valid AccessToken
            // The return object contains the status;
            try {
                createdPayment = payment.create(paypalApiContext);
                logger.info("Created payment with id = " + createdPayment.getId() + " and status = " + createdPayment.getState());
                // ###Payment Approval Url
                Iterator<Links> links = createdPayment.getLinks().iterator();
                while (links.hasNext()) {
                    Links link = links.next();
                    if (link.getRel().equalsIgnoreCase("approval_url")) {
                        //payerId.setAttribute("redirectURL", link.getHref());
                    }
                }
                //ResultPrinter.addResult(payerId, resp, "Payment with PayPal", Payment.getLastRequest(), Payment.getLastResponse(), null);
                map.put(guid, createdPayment.getId());
            } catch (PayPalRESTException e) {
                //ResultPrinter.addResult(payerId, resp, "Payment with PayPal", Payment.getLastRequest(), null, e.getMessage());
            }
        //}
        return createdPayment;
    }

    @Override
    public com.hair.business.beans.entity.Payment pay(Long senderId, Long recipientId, double amount) {
        Payment x = createPayment(1L, 2L);
        return null;
    }

    @Override
    public com.hair.business.beans.entity.Payment refund(Long senderId, Long recipientId, double amount) {
        return null;
    }
}
