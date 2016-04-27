package com.hair.business.cache.repository.ehcache;

import com.hair.business.cache.repository.WriteBehind;

import net.sf.ehcache.CacheEntry;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.writebehind.operations.SingleOperationType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import javax.inject.Named;

/**
 * EhCache write behind impl.
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
@Named
public class WriteBehindImpl implements WriteBehind {

    static final Logger logger = LoggerFactory.getLogger(WriteBehindImpl.class);

    public CacheWriter clone(Ehcache ehcache) throws CloneNotSupportedException {
        logger.error("{} cannot be cloned.", this.getClass());

        return null;
    }

    public void init() {

    }

    public void dispose() throws CacheException {

    }

    public void write(Element element) throws CacheException {
        // TODO writeOne to DB (ES)
    }

    public void writeAll(Collection<Element> collection) throws CacheException {
        // TODO writeMany to DB (ES)
    }

    public void delete(CacheEntry cacheEntry) throws CacheException {

    }

    public void deleteAll(Collection<CacheEntry> collection) throws CacheException {

    }

    public void throwAway(Element element, SingleOperationType singleOperationType, RuntimeException e) {
        logger.error("Write behind error: Could not write object {}, with message: {}.", element.getObjectValue(), e.getMessage());
        //TODO handle DB write failure
    }
}
