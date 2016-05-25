package com.hair.business.services.customer;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Olukorede Aguda on 25/05/2016.
 */
public class CustomerServiceImpl implements CustomerService {

    private String someProperty;

    @Inject
    public CustomerServiceImpl(@Named("app.name") String someProperty) {
        this.someProperty = someProperty;
    }

    public String getSomeProperty() {
        return someProperty;
    }
}
