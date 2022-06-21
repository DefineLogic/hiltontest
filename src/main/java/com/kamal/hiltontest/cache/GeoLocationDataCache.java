package com.kamal.hiltontest.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.kamal.hiltontest.model.GeoLocationData;
import com.kamal.hiltontest.repository.GeoLocationDataDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GeoLocationDataCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoLocationDataCache.class);

    private final GeoLocationDataDao geoLocationDataDao;

    public GeoLocationDataCache(GeoLocationDataDao geoLocationDataDao){
        this.geoLocationDataDao = geoLocationDataDao;
    }

    private LoadingCache<String, GeoLocationData> loadingCache = CacheBuilder.newBuilder()
            .refreshAfterWrite(60, TimeUnit.SECONDS)
            .recordStats()
            .maximumSize(100)
            .build(new CacheLoader<String, GeoLocationData>() {
                @Override
                public GeoLocationData load(String ipAddress) throws Exception {
                    GeoLocationData geoLocationData = geoLocationDataDao.findByIp(ipAddress);
                    return geoLocationData;
                }
            });

    public GeoLocationData getGeoLocationData(String ipAddress) throws ExecutionException{
        try {
            return loadingCache.get(ipAddress);
        }
        catch(CacheLoader.InvalidCacheLoadException e) {
            LOGGER.info("Data is not present in database.");
        }
        return null;
    }

    public CacheStats getCacheStats(){
        return loadingCache.stats();
    }
}
