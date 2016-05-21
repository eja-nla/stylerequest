package com.hair.business;

import com.hair.business.dao.entity.Customer;
import com.hair.business.dao.entity.Location;
import com.hair.business.dao.entity.Merchant;
import com.hair.business.dao.entity.Payment;

/**
 * Created by Olukorede Aguda on 16/05/2016.
 */
public abstract class AbstractServiceTestConstants {

    public static final Customer CUSTOMER = new Customer();

    public static final Merchant MERCHANT = new Merchant();

    public static final Payment PAYMENT = new Payment();

    public static final Location LOCATION = new Location();

    public AbstractServiceTestConstants() {
    }

}
