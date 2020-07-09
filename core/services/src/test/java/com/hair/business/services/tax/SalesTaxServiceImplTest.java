package com.hair.business.services.tax;

import static com.x.y.EntityTestConstants.createAddress;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.hair.business.beans.entity.AddOn;
import com.hair.business.beans.entity.tax.ComputeTaxRequest;
import com.hair.business.beans.entity.tax.ComputeTaxResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

/**
 * Created by ejanla on 7/8/20.
 */
public class SalesTaxServiceImplTest {

    private SalesTaxPalHttpClientImpl salesTaxPalHttpClient = Mockito.mock(SalesTaxPalHttpClientImpl.class);
    private SalesTaxService taxService = new SalesTaxServiceImpl(salesTaxPalHttpClient);

    @Before
    public void setUp() throws Exception {
        Mockito.when(salesTaxPalHttpClient.doPost(Mockito.any(ComputeTaxRequest.class))).thenReturn(new ComputeTaxResponse());
    }

    @Test
    public void computeTax() {
        ComputeTaxResponse res = taxService.computeTax("someid", "didi", 134, createAddress(), createAddress(), Collections.singletonList(new AddOn()));
        Assert.assertThat(res, notNullValue());
    }
}