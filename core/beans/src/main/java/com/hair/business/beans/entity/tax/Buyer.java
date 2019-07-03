package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class Buyer extends AbstractBean {
    private PhysicalOrigin destination;

    public PhysicalOrigin getDestination() {
        return destination;
    }

    public void setDestination(PhysicalOrigin destination) {
        this.destination = destination;
    }
}
