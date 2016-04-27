package com.hair.business.cache.repository;


import net.sf.ehcache.writer.CacheWriter;

/**
 * Write behind - async writes to persistence
 *
 * Created by Olukorede Aguda on 27/04/2016.
 */
public interface CacheWriteBehind extends CacheWriter {
}
