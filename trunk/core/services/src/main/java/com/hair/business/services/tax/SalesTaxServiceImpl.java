package com.hair.business.services.tax;

import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.Address;
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

import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ejanla on 7/8/20.
 */
public class SalesTaxServiceImpl implements SalesTaxService {

    private static final Logger logger = getLogger(SalesTaxServiceImpl.class);


    private final SalesTaxPalHttpClientImpl taxClient;

    public SalesTaxServiceImpl(SalesTaxPalHttpClientImpl taxClient) {
        this.taxClient = taxClient;
    }


    @Override
    public ComputeTaxResponse computeTax(String stylerequestID, String styleName, int servicePrice, Address merchantAddress, Address customerAddress, List<AddOn> addOns) {
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
            AddOn addOn = addOns.get(i-1);
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
}
