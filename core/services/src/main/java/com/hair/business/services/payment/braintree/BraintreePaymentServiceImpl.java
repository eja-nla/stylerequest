package com.hair.business.services.payment.braintree;

import static org.slf4j.LoggerFactory.getLogger;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.hair.business.beans.constants.PaymentType;
import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Address;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.PaymentItem;
import com.hair.business.beans.entity.PaymentMethod;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.StyleRequestPayment;
import com.hair.business.beans.entity.tax.Buyer;
import com.hair.business.beans.entity.tax.ComputeTaxRequest;
import com.hair.business.beans.entity.tax.ComputeTaxResponse;
import com.hair.business.beans.entity.tax.Currency;
import com.hair.business.beans.entity.tax.LineItem;
import com.hair.business.beans.entity.tax.PhysicalOrigin;
import com.hair.business.beans.entity.tax.Product;
import com.hair.business.beans.entity.tax.Quantity;
import com.hair.business.beans.entity.tax.Seller;
import com.hair.business.beans.entity.tax.TaxRequest;
import com.hair.business.beans.entity.tax.UnitPrice;
import com.hair.business.beans.helper.PaymentStatus;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.services.client.retry.RetryWithExponentialBackOff;
import com.hair.business.services.payment.PaymentService;
import com.hair.business.services.tax.SalesTaxPalHttpClientImpl;
import com.x.business.exception.PaymentException;
import com.x.business.utilities.Assert;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Braintree payment processor
 *
 * Created by olukoredeaguda on 06/03/2017.
 */
public class BraintreePaymentServiceImpl implements PaymentService {

    private static final Logger logger = getLogger(BraintreePaymentServiceImpl.class);
    private final BraintreeGateway gateway;
    private final SalesTaxPalHttpClientImpl taxClient;
    private final Repository repository;

    @Inject
    public BraintreePaymentServiceImpl(Provider<BraintreeGateway> gatewayProvider, SalesTaxPalHttpClientImpl taxClient, Repository repository) {
        this.gateway = gatewayProvider.get();
        this.taxClient = taxClient;
        this.repository = repository;
    }

    @Override
    public StyleRequest authorize(String nonce, final Long styleRequestId) {
        final StyleRequest styleRequest = repository.findOne(styleRequestId, StyleRequest.class);
        return authorize(nonce, styleRequest);
    }

    @Override
    public StyleRequest authorize(String nonce, final StyleRequest styleRequest) {
        Assert.notNull(styleRequest, "You cannot authorize a null style request");

        final Style style = styleRequest.getStyle();
        Assert.notNull(style, "You cannot authorize a style request with a null style");

        final Customer customer = styleRequest.getCustomer();
        Assert.notNull(customer.getId(), customer.getPayment());

        final double price = style.getPrice();
        final String stylrequestID = Long.toString(styleRequest.getId());

        final ComputeTaxResponse tax = computeTax(
                stylrequestID,
                style.getName(),
                price,
                styleRequest.getMerchant().getAddress(),
                customer.getAddress(),
                styleRequest.getAddOns()
        );

        final Transaction result = createTransaction(nonce, stylrequestID, customer.getPaymentId(), price, tax.getComputeTaxResponse().getTotalTax(),false);

        final StyleRequestPayment authorizedPayment = createPayment(result, styleRequest.getMerchant().getId(), PaymentStatus.AUTHORIZED, tax);
        styleRequest.setAuthorizedPayment(authorizedPayment);

        logger.info("Successfully authorized payment amount {} for style request {}", authorizedPayment.getTotalAmount(), styleRequest.getId());

        return styleRequest;
    }

    @Override
    public StyleRequest settlePreAuthPayment(final StyleRequest styleRequest) {
        Assert.notNull(styleRequest, "Style request cannot be null");

        final StyleRequestPayment srPayment = styleRequest.getAuthorizedPayment();
        Assert.notNull(srPayment, "Style request authorized payment cannot be null");
        Assert.isTrue(srPayment.getPaymentStatus() == PaymentStatus.AUTHORIZED, "Settling an unauthorized transaction is forbidden");

        final String authorizedId = srPayment.getTransactionId();
        Assert.notNull(authorizedId, "Pre-Authorized Transaction must have an ID");

        final Style style = styleRequest.getStyle();
        final double price = style.getPrice();
        String stylrequestID = Long.toString(styleRequest.getId());

        // the authorized price/tax could have changed, we check again
        final ComputeTaxResponse tax = computeTax(
                stylrequestID,
                style.getName(),
                price,
                styleRequest.getMerchant().getAddress(),
                styleRequest.getCustomer().getAddress(),
                styleRequest.getAddOns()
        );

        final double totalPrice = price + tax.getComputeTaxResponse().getTotalTax();

        final Transaction transaction = settleTransaction(authorizedId, totalPrice);

        final StyleRequestPayment settledPayment = createPayment(transaction, styleRequest.getMerchant().getId(), PaymentStatus.SETTLED, tax);
        styleRequest.setSettledPayment(settledPayment);

        return styleRequest;
    }

    @Override
    public void deductNonPreAuthPayment(String transactionId, List<AddOn> items) {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getAmount();
        }
        settleTransaction(transactionId, total);
    }

    @Override
    public ComputeTaxResponse computeTax(String stylerequestID, String styleName, double servicePrice, Address merchantAddress, Address customerAddress, List<AddOn> addOns) {

        final TaxRequest taxRequest = new TaxRequest(DateTime.now().toString("yyyyMMdd"), "SALE");
        final Currency currency = new Currency();
        currency.setIsoCurrencyCode("USD");
        taxRequest.setCurrency(currency);

        final Seller seller = new Seller();
        final PhysicalOrigin sellerOrigin = new PhysicalOrigin();
        sellerOrigin.setCity(merchantAddress.getLocation().getCity());
        sellerOrigin.setStateOrProvince(merchantAddress.getLocation().getState());
        sellerOrigin.setDistrictOrCounty(merchantAddress.getDistrict());
        sellerOrigin.setPostalCode(merchantAddress.getZipCode());
        sellerOrigin.setCountry(merchantAddress.getLocation().getCountryCode());
        seller.setPhysicalOrigin(sellerOrigin);
        taxRequest.setSeller(seller);

        final Buyer buyer = new Buyer();
        final PhysicalOrigin buyerOrigin = new PhysicalOrigin();
        buyerOrigin.setCity(customerAddress.getLocation().getCity());
        buyerOrigin.setStateOrProvince(customerAddress.getLocation().getState());
        buyerOrigin.setDistrictOrCounty(customerAddress.getDistrict());
        buyerOrigin.setPostalCode(customerAddress.getZipCode());
        buyerOrigin.setCountry(customerAddress.getLocation().getCountryCode());
        buyer.setDestination(buyerOrigin);
        taxRequest.setBuyer(buyer);


        List<LineItem> items = new ArrayList<>(addOns.size() + 1);
            LineItem item = new LineItem(); // we create the first lineItem, which is for the style request itself
            item.setLineItemId(stylerequestID);
            item.setProductName(styleName);
            Product product = new Product();
            product.setClassCode("STP-PCC-00739");
            product.setValue("Styling Service");
            item.setProduct(product);

            UnitPrice unitPrice = new UnitPrice();
            unitPrice.setValue(servicePrice);
            item.setUnitPrice(unitPrice);

            Quantity quantity = new Quantity();
            quantity.setUnitOfMeasure("ea");
            quantity.setValue(1);
            item.setQuantity(quantity);
        items.add(item);

        // we add each more lineItems based on purchased adOns
        for (int i = 1; i <= addOns.size(); i++) { //start from 1 because we use the index as the item ID
            LineItem addonItem = new LineItem();
                AddOn addOn = addOns.get(i);
                addonItem.setLineItemId(Integer.toString(i));
                addonItem.setProductName(addOn.getItemName());
                Product addOnProduct = new Product();
                addOnProduct.setClassCode("STP-PCC-01085");
                addOnProduct.setValue(addOn.getItemName());
                addonItem.setProduct(product);
    
                UnitPrice addonPrice = new UnitPrice();
                addonPrice.setValue(addOn.getAmount());
                addonItem.setUnitPrice(addonPrice);
    
                Quantity addonQtty = new Quantity();
                addonQtty.setUnitOfMeasure("ea");
                addonQtty.setValue(addOn.getQuantity());
                addonItem.setQuantity(addonQtty);
            items.add(addonItem);
        }
        
        taxRequest.setLineItems(items);
        ComputeTaxRequest computeTaxRequest = new ComputeTaxRequest(taxRequest);
        ComputeTaxResponse computeTaxResponse;

        try {
            computeTaxResponse = taxClient.doPost(computeTaxRequest);
        } catch (IOException e) {
            logger.warn("Unable to process tax : StylerequestID={} Request={} Response={null}", stylerequestID, computeTaxRequest);
            throw new RuntimeException(e);
        }
        return computeTaxResponse;
    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, boolean isDefault) {

    }

    @Override
    public void updatePayment(Long customerId, PaymentMethod paymentMethod, PaymentType paymentType, String nonce, boolean isDefault) {
        final Customer customer = repository.findOne(customerId, Customer.class);
        customer.getPayment().getPaymentItems().add(new PaymentItem(paymentType, paymentMethod, isDefault));
        addPaymentMethod(nonce, paymentMethod);

        repository.saveOne(customer);
    }

    @Override
    public void refund(Long styleRequestId, double amount) {

        Assert.notNull(styleRequestId);

        final StyleRequest request = repository.findOne(styleRequestId, StyleRequest.class);
        Assert.notNull(request, "Stylerequest with ID " + styleRequestId + " cannot be found");
        Assert.notNull(request.getSettledPayment(), "Cannot refund a Style request ID:" + styleRequestId + " with no settled payment");

        refund(Long.toString(request.getSettledPayment().getId()), new BigDecimal(amount));
    }

    @Override
    public Result refund(String transactionId, BigDecimal amount) {
        Result<Transaction> result;
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            result = RetryWithExponentialBackOff.execute(() -> gateway.transaction().refund(transactionId));
        } else {
            result = RetryWithExponentialBackOff.execute(() -> gateway.transaction().refund(transactionId, amount));
        }
        if (result != null && !result.isSuccess()){
            logger.error("Braintree refund request failed: {}", result.getMessage());
        }

        return result;
    }

    @Override
    public Transaction settleTransaction(String transactionId, double amount) {
        final Result<Transaction> result = RetryWithExponentialBackOff.execute(() -> gateway.transaction().submitForSettlement(transactionId, BigDecimal.valueOf(amount)));

        if (!result.isSuccess()) {
            throw new PaymentException("Braintree settle transaction request failed: " + result.getMessage());
        }
        return result.getTarget();
    }


    @Override
    public String issueClientToken(final String entityId) {
        final ClientTokenRequest clientTokenRequest = new ClientTokenRequest();

        if (StringUtils.isNotEmpty(entityId)) {
            clientTokenRequest.customerId(entityId);
        }

        final String result = RetryWithExponentialBackOff.execute(() -> gateway.clientToken().generate(clientTokenRequest));

        if (StringUtils.isEmpty(result)){
            logger.warn("Failed to issue token for entity {}", entityId);
        }
        return result;
    }

    @Override
    public String createProfile(String userId, String nonce) {
        final CustomerRequest request = new CustomerRequest()
                .customerId(userId)
                .paymentMethodNonce(nonce);

        final Result<com.braintreegateway.Customer> result = RetryWithExponentialBackOff.execute(() -> gateway.customer().create(request));

        if (!result.isSuccess()){
            logger.error("Braintree create customer request failed: {}", result.getMessage());
        }

        return result.getTarget().getId();
    }


    @Override
    public Transaction createTransaction(String nonce, String orderId, String paymentId, double totalAmount, double taxAmount, boolean isSettled) {
        Assert.notNull(paymentId,"Payment ID cannot be null");
        Assert.notNull(nonce, "Nonce cannot be null");
        final TransactionRequest request = new TransactionRequest()
                .customerId(paymentId)
                .amount(BigDecimal.valueOf(totalAmount))
                .taxAmount(BigDecimal.valueOf(taxAmount)) //important!! otherwise the interchange reduction won't apply
                .orderId(orderId)
                .paymentMethodNonce(nonce)
                .options()
                .submitForSettlement(isSettled)
                .done();

        final Result result = RetryWithExponentialBackOff.execute(() -> gateway.transaction().sale(request));

        if (!result.isSuccess()){
            throw new PaymentException("Payment authorization failed with message: " + result.getMessage());
        }
        return (Transaction) result.getTarget();
    }


    @Override
    public boolean addPaymentMethod(String nonce, PaymentMethod paymentMethod) {
        final PaymentMethodRequest request = new PaymentMethodRequest()
                .customerId(paymentMethod.getCustomerId())
                .paymentMethodNonce(nonce);

        final Result<? extends com.braintreegateway.PaymentMethod> result = RetryWithExponentialBackOff.execute(() -> gateway.paymentMethod().create(request));

        if (!result.isSuccess()){
            logger.error("Braintree add new payment method request failed: {}", result.getMessage());
        }

        return result.isSuccess();
    }

    private StyleRequestPayment createPayment(Transaction transaction, Long merchantId, PaymentStatus paymentStatus, ComputeTaxResponse tax){
        final StyleRequestPayment payment = new StyleRequestPayment(
                transaction.getAmount().doubleValue(),
                Long.valueOf(transaction.getCustomer().getId()),
                merchantId,
                PaymentStatus.SETTLED == paymentStatus
        );
        payment.setPaymentStatus(paymentStatus);
        payment.setTransactionId(transaction.getId());
        payment.setTaxDetails(tax);

        return payment;

    }

}
