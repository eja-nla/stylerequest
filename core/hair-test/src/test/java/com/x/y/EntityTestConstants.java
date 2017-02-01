package com.x.y;

import static java.util.Collections.singletonList;

import com.hair.business.beans.constants.DeviceType;
import com.hair.business.beans.constants.Gender;
import com.hair.business.beans.constants.MerchantType;
import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.Address;
import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Device;
import com.hair.business.beans.entity.GeoPointExt;
import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Location;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Payment;
import com.hair.business.beans.entity.Review;
import com.hair.business.beans.entity.Style;
import com.hair.business.beans.entity.StyleRequest;

import org.joda.time.DateTime;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Persistent entity test constants
 * Every test entity should be created here
 *
 * Created by Olukorede Aguda on 20/06/2016.
 */
public class EntityTestConstants {

    public static Address createAddress() {
        Address address =  new Address("flat x", "SE5 tgg", createLocation());
        address.setId(new Random().nextLong());
        address.setPermanentId(new Random().nextLong());
        return address;

    }

    public static Customer createCustomer(){
        Customer c = new Customer("Test Customer Name", new Random().nextInt(), "customer@email.com", "+4453635436237", createDevice(), createLocation());
        c.setId(new Random().nextLong());
        c.setPermanentId(new Random().nextLong());
        c.setPhotoUrl("http://some.photo.url");
        c.setGender(Gender.M);
        return c;
    }

    public static Device createDevice(){
        Device d =  new Device(new BigInteger(130, new SecureRandom()).toString(32), "ios9", DeviceType.IOS);
        d.setPermanentId(new Random().nextLong());
        d.setId(new Random().nextLong());
        return d;
    }

    public static Image createImage(){
        Image i = new Image("http//"+ new BigInteger(130, new SecureRandom()).toString(32) + ".com", "test owner name", 20);
        i.setId(new Random().nextLong());
        i.setPermanentId(i.getId());
        return i;
    }

    public static Location createLocation(){
        GeoPointExt g = new GeoPointExt(51.5034070, -0.1275920);
        g.setId(new Random().nextLong());
        Location l = new Location("London", "Alabama", "UK", g);
        l.setId(new Random().nextLong());
        l.setPermanentId(l.getId());

        return l;
    }

    public static Merchant createMerchant(){
        Merchant m = new Merchant("Test Merchant Name", new Random().nextInt(), "merchant@email.com", "+1134555643654", createDevice(), createLocation());
        m.setId(new Random().nextLong());
        m.setPermanentId(m.getId());
        m.setPhotoUrl("http://some.photo.url");
        m.setGender(Gender.F);
        return m;
    }

    public static Payment createPayment(){
        Payment p = new Payment(new Random().nextLong(), createCustomer(), createMerchant(), true, MerchantType.PAYPAL);
        p.setId(new Random().nextLong());
        p.setPermanentId(p.getId());
        return p;
    }

    public static Review createReview(){
        Review r = new Review("author's name here", "owner's name here", new Random().nextLong(), new Random().nextLong(), new Random().nextInt(), "Perfect review");
        r.setId(new Random().nextLong());
        r.setPermanentId(r.getId());
        return r;
    }

    public static StyleRequest createStyleRequest(){
        StyleRequest s = new StyleRequest(createStyle(), createMerchant(), createCustomer(), createLocation(), StyleRequestState.ACCEPTED, DateTime.now(), DateTime.now().plusMinutes(6));
        s.setId(new Random().nextLong());
        s.setPermanentId(s.getId());
        return s;
    }

    public static Style createStyle(){
        Merchant m = createMerchant();
        Style s = new Style("didi", 20, m.getId(), m.getLocation(), singletonList(createImage()));
        s.setId(new Random().nextLong());
        s.setPermanentId(new Random().nextLong());
        return s;
    }
}
