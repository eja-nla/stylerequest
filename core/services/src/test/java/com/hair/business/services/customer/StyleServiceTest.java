package com.hair.business.services.customer;

import static com.x.y.EntityTestConstants.createImage;
import static com.x.y.EntityTestConstants.createMerchant;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.y.EntityTestConstants;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Olukorede Aguda on 29/08/2016.
 *
 *
 */
public class StyleServiceTest extends AbstractServicesTestBase {

    StyleService styleService;
    Repository repository;


    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        styleService = new StyleServiceImpl(repository);
    }

    @Test
    public void createStyle() throws Exception {
        Merchant merchant = createMerchant();
        repository.saveOne(merchant);

        Style style = styleService.createStyle("Test style", merchant.getId(), Arrays.asList(createImage()));

        assertThat(style, is(notNullValue()));

    }

    @Test
    public void updateStyle() throws Exception {
        Style style = EntityTestConstants.createStyle();
        repository.saveOne(style);

        styleService.updateStyle(style.getId(), Arrays.asList(createImage()));

        assertThat(style.getStyleImages().size(), is(2));

    }

}