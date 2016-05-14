package com.hair.business.services.actors;

import com.hair.business.dao.entity.Merchant;
import com.hair.business.services.stereotype.PersistenceService;

/**
 * Merchant Service.
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
public interface MerchantService extends PersistenceService<Merchant, String> {

    Merchant findMerchant(String id);
}
