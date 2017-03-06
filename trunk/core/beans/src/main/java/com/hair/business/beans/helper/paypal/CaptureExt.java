package com.hair.business.beans.helper.paypal;

import com.googlecode.objectify.annotation.Serialize;
import com.paypal.api.payments.Capture;

/**
 * Capture extension. We need to do this in order to be able to save the paypal objects
 * excluding the fields that don't play well with Datastore because they have recursive fields
 *
 * Created by olukoredeaguda on 04/03/2017.
 */
@Serialize
public class CaptureExt extends Capture {

}
