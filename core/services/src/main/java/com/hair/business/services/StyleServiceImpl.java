package com.hair.business.services;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import com.hair.business.beans.entity.GeoPointExt;
import com.hair.business.beans.entity.Image;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.Style;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.impl.HairstyleElasticsearchRepositoryExt;
import com.x.business.exception.EntityNotFoundException;
import com.x.business.utilities.Assert;

import org.slf4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Style Service impl
 *
 * Created by Olukorede Aguda on 20/09/2016.
 */
public class StyleServiceImpl implements StyleService {

    private final Repository datastoreRepository;
    private final HairstyleElasticsearchRepositoryExt hairstyleRepository;

    private static final Logger logger = getLogger(StyleServiceImpl.class);

    @Inject
    public StyleServiceImpl(Repository repository, HairstyleElasticsearchRepositoryExt hairstyleRepository) {
        this.datastoreRepository = repository;
        this.hairstyleRepository = hairstyleRepository;
    }

    @Override
    public Style findStyle(Long styleId) {
        return hairstyleRepository.findOne(styleId, Style.class);
    }

    @Override
    public InputStream geoFind(GeoPointExt point, int radius, int pageSize) {
        //Right now, we do nothing to enrich the request or response but mandate that
        // all queries come through backend for tracking and future enrichment.

        return hairstyleRepository.searchRadius(HairstyleElasticsearchRepositoryExt.DISTANCE_QUERY, radius, point, pageSize);
    }

    @Override
    public Style publishStyle(Style style, Long publisherId) {
        Assert.notNull(style, publisherId);

        logger.info("Publishing new style for publisher {}", publisherId);

        Assert.isTrue(style.getStyleImages().size() >= 5, "Style must have at least 5 images showing different views");

        final Merchant merchant = datastoreRepository.findOne(publisherId, Merchant.class);

        Assert.notNull(merchant, format("Could not find Merchant with id '%s'", publisherId));

        final Long stylePermId = datastoreRepository.allocateId(Style.class);

        style.setId(stylePermId);
        style.setPermanentId(stylePermId);
        style.setActive(true);
        style.setLocation(merchant.getAddress().getLocation());

        hairstyleRepository.saveOne(style);
        datastoreRepository.saveOne(style);

        logger.info("Style {} published Successfully for publisher {}", stylePermId, publisherId);

        return style;
    }

    @Override
    public void updateStyle(Style style) throws IllegalArgumentException, EntityNotFoundException {
        Assert.notNull(style, "Style cannot be null");
        Assert.validId(datastoreRepository.peekOne(style.getId(), Style.class));

        logger.info("Updating style {}", style.getId());

        hairstyleRepository.saveOne(style);
        datastoreRepository.saveOne(style);

        logger.info("Style {} updated Successfully.", style.getId());

    }

    @Override
    public void updateStyleImages(Long styleId, List<Image> styleImages) {
        final Style style = datastoreRepository.findOne(styleId, Style.class);

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
    public InputStream search(String term) {
        return hairstyleRepository.search(term);
    }

    @Override
    public InputStream geoSearch(String term, int radius, double lat, double lon) {
        return hairstyleRepository.geoTermSearch(term, radius, lat, lon);
    }

    @Override
    public void removeStyle(Long styleId) {
        Assert.validId(styleId);

        logger.info("Attempting to remove Style {}", styleId);

        Style style = datastoreRepository.findOne(styleId, Style.class);

        Assert.notNull(style, String.format("Unable to remove style with id '%s'. Style not found", styleId));

        style.setActive(false);

        hairstyleRepository.saveOne(style);
        datastoreRepository.saveOne(style);

        logger.info("Style {} removed Successfully.");

    }

    @Override
    public InputStream scroll(String scroll, String scrollId) {
        //what value are we adding by doing this?
        return hairstyleRepository.fetchScroll(scroll, scrollId);
    }

}
