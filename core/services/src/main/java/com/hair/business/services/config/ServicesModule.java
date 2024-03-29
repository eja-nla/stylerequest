package com.hair.business.services.config;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.hair.business.services.StyleRequestService;
import com.hair.business.services.StyleRequestServiceImpl;
import com.hair.business.services.StyleService;
import com.hair.business.services.StyleServiceImpl;
import com.hair.business.services.customer.CustomerService;
import com.hair.business.services.customer.CustomerServiceImpl;
import com.hair.business.services.merchant.MerchantService;
import com.hair.business.services.merchant.MerchantServiceImpl;
import com.hair.business.services.metrics.ExecTimeLoggerInterceptor;
import com.hair.business.services.payment.stripe.StripePaymentService;
import com.hair.business.services.payment.stripe.StripePaymentServiceImpl;
import com.hair.business.services.pushNotification.PushNotificationServiceInternal;
import com.hair.business.services.pushNotification.PushNotificationServiceNoopImpl;
import com.hair.business.services.state.StylerequestStateMgr;
import com.hair.business.services.state.StylerequestStateMgrImpl;
import com.hair.business.services.stereotype.Timed;
import com.hair.business.services.tax.SalesTaxPalHttpClientImpl;

import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Singleton;

/**
 * Created by Olukorede Aguda on 24/05/2016.
 *
 * Services module
 */
public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(CustomerService.class).to(CustomerServiceImpl.class).in(Singleton.class);
        bind(MerchantService.class).to(MerchantServiceImpl.class).in(Singleton.class);
        bind(StyleService.class).to(StyleServiceImpl.class).in(Singleton.class);
        bind(StyleRequestService.class).to(StyleRequestServiceImpl.class).in(Singleton.class);
        bind(StylerequestStateMgr.class).to(StylerequestStateMgrImpl.class).in(Singleton.class);
        bind(PushNotificationServiceInternal.class).to(PushNotificationServiceNoopImpl.class).in(Singleton.class);
        bind(StripePaymentService.class).to(StripePaymentServiceImpl.class).in(Singleton.class);

        bind(SalesTaxPalHttpClientImpl.class).in(Singleton.class);

        bindInterceptor(any(), NotSyntheticMethodMatcher.INSTANCE.and(annotatedWith(Timed.class)), new ExecTimeLoggerInterceptor());

    }

    @Singleton
    @Provides
    public CloseableHttpClient clientProvider() {
        return HttpClients.custom().setSSLSocketFactory(createSSLContext()).build();
    }

    private SSLConnectionSocketFactory createSSLContext(){
        try {
            return new SSLConnectionSocketFactory(SSLContextBuilder.create().useProtocol("TLSv1.2").build(),
                    new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
                    null, new DefaultHostnameVerifier());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }
}
