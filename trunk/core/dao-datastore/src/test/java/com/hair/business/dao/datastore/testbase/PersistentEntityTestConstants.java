package com.hair.business.dao.datastore.testbase;

import com.google.appengine.api.search.GeoPoint;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Payment;

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

    static void init(){
        PAYMENT = new Payment();
        LOCATION = new Location();
        DEVICE = new Device();
        CUSTOMER = new Customer("testName", "testRating", "testEmail@test.com", "+0203443354324", DEVICE, LOCATION, PAYMENT);

        CUSTOMER.setId(new Long(135565));

        PAYMENT.setAmount(2);
        PAYMENT.setFrom(CUSTOMER.getId());

        LOCATION.setCity("London");
        LOCATION.setCountry("UK");
        LOCATION.setGeoPoint(new GeoPoint(51.5034070, -0.1275920)); // No 10 downing street

    }
}
