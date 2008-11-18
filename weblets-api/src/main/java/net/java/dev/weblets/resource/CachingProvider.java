package net.java.dev.weblets.resource;

/**
 * @author werpu
 * @date: 18.11.2008
 * <p/>
 * Generic caching provider
 * this one will be hookable
 * by own implementations
 */
public interface CachingProvider {

    /**
     * returns a cache for a given caching region
     *
     * @param region the region key
     * @return a cache implementation, can be any cache with any behavior
     *         it just must implement the cache interface!
     */
    public Cache getCache(String region);
}
