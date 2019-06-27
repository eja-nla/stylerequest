package com.hair.business.services;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.impl.HairstyleElasticsearchRepositoryImpl;
import com.x.business.exception.EntityNotFoundException;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Style Service impl
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public class StyleServiceImpl implements StyleService {

    private final Repository datastoreRepository;
    private final HairstyleElasticsearchRepositoryImpl hairstyleRepository;

    private static final Logger logger = getLogger(StyleServiceImpl.class);

    @Inject
    public StyleServiceImpl(Repository repository, HairstyleElasticsearchRepositoryImpl hairstyleRepository) {
        this.datastoreRepository = repository;
        this.hairstyleRepository = hairstyleRepository;
    }

    @Override
    public Style findStyle(Long styleId) {
        return hairstyleRepository.findOne(styleId, Style.class);
    }

    @Override
    public Style publishStyle(Style style, Long publisherId) {
        Assert.notNull(style, publisherId);
        Assert.isTrue(style.getStyleImages().size() >= 5, "Style must have at least 5 images showing different views");

        Merchant merchant = datastoreRepository.findOne(publisherId, Merchant.class);

        Assert.notNull(merchant, format("Could not find Merchant with id '%s'", publisherId));

        Long stylePermId = datastoreRepository.allocateId(Style.class);

        style.setId(stylePermId);
        style.setPermanentId(stylePermId);
        style.setActive(true);
        style.setLocation(merchant.getAddress().getLocation());

        hairstyleRepository.saveOne(style);
        datastoreRepository.saveOne(style);

        return style;
    }

    @Override
    public void updateStyle(Style style) throws IllegalArgumentException, EntityNotFoundException {
        Assert.notNull(style, "Style cannot be null");
        Assert.validId(datastoreRepository.peekOne(style.getId(), Style.class));

        hairstyleRepository.saveOne(style);
        datastoreRepository.saveOne(style);

    }

    @Override
    public void updateStyleImages(Long styleId, List<Image> styleImages) {
        Style style = datastoreRepository.findOne(styleId, Style.class);

        Assert.notNull(style, format("Could not find Style with id '%s'", styleId));

        style.getStyleImages().addAll(styleImages);

        hairstyleRepository.saveOne(style);
        datastoreRepository.saveOne(style);

    }

    @Override
    public List<Style> findStyles(List<Long> ids) {
        return new ArrayList<>(datastoreRepository.findMany(ids, Style.class).values());
    }

    @Override
    public List<Style> findStylesByDescription(String description) {
        return null;
    }

    @Override
    public void removeStyle(Long styleId) {
        Assert.validId(styleId);
        Style style = datastoreRepository.findOne(styleId, Style.class);

        Assert.notNull(style, String.format("Cannot remove style with id '%s'. Style not found", styleId));

        style.setActive(false);

        hairstyleRepository.saveOne(style);
        datastoreRepository.saveOne(style);
    }

    @Override
    public Map<String, List<Style>> proximitySearchByZipcode(List<Integer> zipcodes, int limit, String cursorStr) {

        return datastoreRepository.searchWithCursor("zipcode in", zipcodes, Style.class, limit, cursorStr);
    }

    @Override
    public List<Style> proximitySearchByGeo() {
        return null;
    }
}
