package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;

import java.util.List;

/**
 * Created by olukoredeaguda on 09/02/2017.
 *
 *
 */
public class PaymentInformation extends AbstractActorEnablerEntity {

    @Id
    private Long id;

    Long ownerId;
    
    private List<CreditCard> creditCards;

}
