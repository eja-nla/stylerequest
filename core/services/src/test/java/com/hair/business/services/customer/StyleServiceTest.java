package com.hair.business.services.customer;

import static com.x.y.EntityTestConstants.createImage;
import static com.x.y.EntityTestConstants.createMerchant;
import static com.x.y.EntityTestConstants.createStyle;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.impl.HairstyleElasticsearchRepositoryExt;
import com.hair.business.services.StyleService;
import com.hair.business.services.StyleServiceImpl;
import com.x.y.EntityTestConstants;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Olukorede Aguda on 29/08/2016.
 *
 *
 */
public class StyleServiceTest extends AbstractServicesTestBase {

    private StyleService styleService;
    private Repository repository;


    @Before
    public void setUp(){
        repository = injector.getInstance(Repository.class);
        styleService = new StyleServiceImpl(repository, Mockito.mock(HairstyleElasticsearchRepositoryExt.class));
    }

    @Test
    public void testPublishStyle() throws Exception {
        Merchant merchant = createMerchant();
        repository.saveOne(merchant);

        Style style = styleService.publishStyle(createStyle(), merchant.getId());

        assertThat(style, is(notNullValue()));
        assertThat(style.isActive(), is(true));
        assertThat(style.getLocation(), is(notNullValue()));

        assertThat(repository.findOne(style.getId(), Style.class), is(notNullValue()));
    }

    @Test
    public void testUpdateStyle() {
        Style style = EntityTestConstants.createStyle();
        repository.saveOne(style);

        assertThat(style.getStyleImages().size(), is(5));

        styleService.updateStyleImages(style.getId(), singletonList(createImage()));

        assertThat(style.getStyleImages().size(), is(6));

    }

    @Test
    public void testFindStyles() throws Exception {
        Collection<Style> styles = createStyles();
        repository.saveMany(styles);

        List<Long> x = styles.stream().map(Style::getId).collect(Collectors.toList());
        MatcherAssert.assertThat(styleService.findStyles(x).size(), Is.is(5));

        repository.delete(styles.toArray());

    }

    private List<Style> createStyles() throws Exception {
        List<Style> styles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            styles.add(EntityTestConstants.createStyle());
        }
        return styles;
    }

}