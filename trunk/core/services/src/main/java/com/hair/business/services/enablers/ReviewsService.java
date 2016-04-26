package com.hair.business.services.enablers;

import com.hair.business.dao.entity.Customer;
import com.hair.business.dao.entity.Merchant;
import com.hair.business.dao.entity.Review;
import com.hair.business.services.stereotype.PersistenceService;

/**
 * Reviews service.
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
public interface ReviewsService extends PersistenceService<Customer, String> {

    void saveCustomerReview(Customer customer, Review review);

    void saveMerchantReview(Merchant merchant, Review review);

    void findCustomerReview(Customer customer, Merchant merchant);

    void findMerchantReview(Merchant customer);

}
