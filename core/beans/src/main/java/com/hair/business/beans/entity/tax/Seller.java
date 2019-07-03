package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class Seller extends AbstractBean {
    private PhysicalOrigin physicalOrigin;

    public PhysicalOrigin getPhysicalOrigin() {
        return physicalOrigin;
    }

    public void setPhysicalOrigin(PhysicalOrigin physicalOrigin) {
        this.physicalOrigin = physicalOrigin;
    }
}
