package net.java.dev.weblets.caching;

/**
 * @author werpu
 * @date: 18.11.2008
 * <p/>
 * A simple caching interface
 * all caches must implement it!
 */
public interface Cache {

    /**
     * getter for the cache
     *
     * @param key a simple key
     * @return the cache entry or null depending whether the cache was hit or not
     */
    public Object get(String key);

    /**
     * the put for the key
     *
     * @param key  the unique key receiving the cache data
     * @param data the neutral cache data containing all which has to be cached
     */
    public void put(String key, Object data);

    /**
     * clears the entire cache
     */
    public void flush(); /*emty the cache no matter what!*/

    /**
     * checks if the cache limit has been reached
     *
     * @return true if no more elements are accepted
     */
    public boolean full();
}
