package com.hair.business.services;

import static java.lang.String.format;

import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.business.exception.EntityNotFoundException;
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
        Assert.notNull(styleImages, styleName, duration, publisherId);
        Assert.isTrue(styleImages.size() >= 5, "Style must have at least 5 images showing different views");

        Merchant merchant = repository.findOne(publisherId, Merchant.class);

        Assert.isFound(merchant, format("Could not find Merchant with id %s", publisherId));

        Long stylePermId = repository.allocateId(Style.class);
        Style style = new Style(styleName, duration, merchant.getId(), merchant.getLocation(), styleImages);

        style.setId(stylePermId);
        style.setPermanentId(stylePermId);
        style.setActive(true);
        repository.saveOne(style);

        return style;
    }

    @Override
    public void updateStyle(Style style) throws IllegalArgumentException, EntityNotFoundException {
        Assert.notNull(style, "Style cannot be null");
        Assert.validId(repository.peekOne(style.getId(), Style.class));

        repository.saveOne(style);

    }

    @Override
    public void updateStyleImages(Long styleId, List<Image> styleImages) {
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
    public void removeStyle(Long styleId) {
        Assert.validId(styleId);
        Style style = repository.findOne(styleId, Style.class);

        Assert.notNull(style, String.format("Cannot remove style with id %s. Style not found", styleId));

        style.setActive(false);

        repository.saveOne(style);
    }
}
