package com.hair.business.cache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


/**
 * Main services config
 *
 * Created by Olukorede Aguda on 26/04/2016.
 */
@Configuration
@EnableCaching
@ComponentScan({ "com.hair.business.cache" })
public class CacheConfiguration {

    @Bean
    public CacheManager customCacheManager(@Value("${ehcache.config.filepath}") String ehcacheXml) {
        return new EhCacheCacheManager(ehCacheCacheManager(ehcacheXml).getObject());
    }

    public EhCacheManagerFactoryBean ehCacheCacheManager(String ehcacheXml) {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource(ehcacheXml));
        cmfb.setShared(true);
        return cmfb;
    }

}
