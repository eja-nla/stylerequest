package com.hair.business.cache.repository.ehcache;

import com.hair.business.cache.repository.WriteBehind;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.CacheWriterFactory;

import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Olukorede Aguda on 27/04/2016.
 */
@Named
public class WritebehindFactory extends CacheWriterFactory {

    private WriteBehind writeBehind;

    @Inject
    public WritebehindFactory(WriteBehind writeBehind) {
        this.writeBehind = writeBehind;
    }

    @Override
    public CacheWriter createCacheWriter(Ehcache ehcache, Properties properties) {
        return writeBehind;
    }
}
