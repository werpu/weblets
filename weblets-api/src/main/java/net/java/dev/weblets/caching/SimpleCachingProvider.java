package net.java.dev.weblets.caching;

import net.java.dev.weblets.caching.SimpleNonVolatileCache;
import net.java.dev.weblets.caching.SimpleVolatileLRUCache;
import net.java.dev.weblets.caching.CachingProvider;
import net.java.dev.weblets.caching.Cache;

/**
 * @author werpu
 * @date: 18.11.2008
 * <p/>
 * A simple cache provider factory
 * providing various cache instances of
 */
public class SimpleCachingProvider implements CachingProvider {

    static SimpleCachingProvider _instance = new SimpleCachingProvider();

    public static SimpleCachingProvider getInstance() {
        return _instance;
    }

    /*Cache regions*/
    SimpleVolatileLRUCache resourceDataCache = new SimpleVolatileLRUCache(100);
    SimpleNonVolatileCache tempfileCache = new SimpleNonVolatileCache();

    public Cache getCache(String region) {
        if (region.equals("resourceData")) {
            return resourceDataCache;
        }
        if(region.equals("tempfileCache")) {
            return tempfileCache;
        }
        return null;
    }
}
