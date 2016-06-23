package com.x.y;

import com.hair.business.beans.constants.MerchantType;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.GeoPointExt;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Payment;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;

import org.joda.time.DateTime;

import java.util.Random;

/**
 * Persistent entity test constants
 * Every test entity should be created here
 *
 * Created by Olukorede Aguda on 20/06/2016.
 */
public class PersistentEntityTestConstants {

    public static Payment PAYMENT;
    public static Customer CUSTOMER;
    public static Location LOCATION;
    public static Device DEVICE;
    public static Merchant MERCHANT;
    public static Style STYLE;

    public static void init(){
        PAYMENT = new Payment(new Random().nextLong(), createCustomer(), createMerchant(), true, MerchantType.PAYPAL, DateTime.now());
        PAYMENT.setId(new Random().nextLong());

        LOCATION = new Location("London", "Alabama", "UK", new GeoPointExt(51.5034070, -0.1275920));
        DEVICE = new Device();

        PAYMENT.setAmount(2);

        LOCATION.setCity("London");
        LOCATION.setCountry("UK");
        LOCATION.setGeoPoint(new GeoPointExt(51.5034070, -0.1275920)); // No 10 downing street

        CUSTOMER = new Customer("testName", 2, "testEmail@test.com", "+0203443354324", DEVICE, LOCATION, PAYMENT);

        CUSTOMER.setId(new Random().nextLong());

        MERCHANT = new Merchant("Test seed Merchant Name", new Random().nextInt(), "merchant0@email.com", "+1134555643654", DEVICE, LOCATION, PAYMENT);
        MERCHANT.setId(new Random().nextLong());

        STYLE = new Style(new Random().nextLong(), true, true, LOCATION);
        STYLE.setId(new Random().nextLong());

    }

    public static Customer createCustomer(){
        Customer c = new Customer("Test Customer Name", new Random().nextInt(), "customer@email.com", "+4453635436237", DEVICE, LOCATION != null? LOCATION:null, PAYMENT!=null? PAYMENT:null);
        c.setId(new Random().nextLong());
        return c;
    }

    public static Merchant createMerchant(){
        Merchant m = new Merchant("Test Merchant Name", new Random().nextInt(), "merchant@email.com", "+1134555643654", new Device(), LOCATION != null? LOCATION:null, PAYMENT!=null? PAYMENT:null);
        m.setId(new Random().nextLong());
        return m;
    }

    public static Payment createPayment(){
        Payment p = new Payment(new Random().nextLong(), CUSTOMER, MERCHANT, true, MerchantType.PAYPAL, DateTime.now());
        p.setId(new Random().nextLong());
        return p;
    }

    public static Location createLocation(){
        return new Location("London", "Alabama", "UK", new GeoPointExt(51.5034070, -0.1275920));
    }

    public static StyleRequest createStyleRequest(){
        StyleRequest s = new StyleRequest(STYLE, MERCHANT, CUSTOMER, LOCATION, StyleRequestState.ACCEPTED, DateTime.now());
        s.setId(new Random().nextLong());
        return s;
    }

    public static Style createStyle(){
        Style s = new Style(new Random().nextLong(), true, true, LOCATION);
        return s;
    }
}
