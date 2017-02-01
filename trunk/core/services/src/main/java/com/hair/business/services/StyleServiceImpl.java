package com.hair.business.services;

import static java.lang.String.format;

import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.business.utilities.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Style Service impl
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public class StyleServiceImpl implements StyleService {

    private final Repository repository;

    @Inject
    public StyleServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Style findStyle(Long styleId) {
        return repository.findOne(styleId, Style.class);
    }

    @Override
    public Style publishStyle(String styleName, int duration, Long publisherId, List<Image> styleImages) {
        Merchant merchant = repository.findOne(publisherId, Merchant.class);

        Assert.isFound(merchant, format("Could not find Merchant with id %s", publisherId));

        Long stylePermId = repository.allocateId(Style.class);
        Style style = new Style(styleName, duration, merchant.getId(), merchant.getLocation(), styleImages);

        style.setId(stylePermId);
        style.setPermanentId(stylePermId);
        repository.saveOne(style);

        return style;
    }

    @Override
    public void updateStyle(Long styleId, List<Image> styleImages) {
        Style style = repository.findOne(styleId, Style.class);

        Assert.isFound(style, format("Could not find Style with id %s", styleId));

        Collection<Image> images = new ArrayList<>();

        images.addAll(style.getStyleImages());
        images.addAll(styleImages);

        style.setStyleImages(images);
        repository.saveOne(style);

    }

    @Override
    public List<Style> findStyles(List<Long> ids) {
        return new ArrayList<>(repository.findMany(ids, Style.class).values());
    }

    @Override
    public List<Style> findStylesByDescription(String description) {
        return null;
    }

    @Override
    public void publishStyle(Style style, Merchant merchant) {

    }
}
