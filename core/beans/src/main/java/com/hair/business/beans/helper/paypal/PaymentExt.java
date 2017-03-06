package com.hair.business.beans.helper.paypal;

import com.googlecode.objectify.annotation.Serialize;
import com.paypal.api.payments.Payment;

/**
 * Payment extension. We need to do this in order to be able to save the paypal objects
 * excluding some fields that don't play well with Datastore because they have recursive fields
 *
 * Created by olukoredeaguda on 04/03/2017.
 */
@Serialize
public class PaymentExt extends Payment {

}
