package com.hair.business.beans.entity.tax;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by Kore Aguda on 6/30/19.
 */
public class Product extends AbstractBean {
    private String classCode; //Hair Salon Services=STP-PCC-00739, 	Hair Care Products=STP-PCC-01085. see https://www.salestaxpal.com/classCodes.jsp
    private String value; // returned in the taxresponse as productName

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
