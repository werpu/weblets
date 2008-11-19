package net.java.dev.weblets.caching;

import net.java.dev.weblets.resource.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author werpu
 * @date: 18.11.2008
 * <p/>
 * A simple volatile LRU cache which can be replaced by other implementations
 * this cache is a mixture of simplicity and performance
 * for a multithreaded environment
 * we divide into an array of cache regions
 * to reduce the locking on various resources
 * every resource key is assigned by a simple
 * hashkey calculation to a certain subregion
 * which should be enough to reduce locking to a big
 * extent!
 */
public class SimpleVolatileLRUCache implements Cache {

    /**
     * we split the cache into 10 internal regions to speed things up
     */
    Map[] _subregions = null;
    int _capacity;
    private int _subregionCnt = 20;

    private static int DIST_WEIGHT = 10; //distribution weight also the number of parallel threads per access

    public SimpleVolatileLRUCache(int capacity) {
        _capacity = capacity;
        _subregionCnt = Math.max(3, capacity / DIST_WEIGHT);  //at least 3 lru regions!
        _subregions = new Map[_subregionCnt];
        for (int cnt = 0; cnt < _subregionCnt; cnt++) {
            _subregions[cnt] = new LinkedHashMap(1, 0.75f, true) {
                /*put already under synchronized remove should not do anything wild*/
                protected boolean removeEldestEntry(final Map.Entry eldest) {
                    return size() > _capacity;
                }
            };
        }
    }

    public Object get(String key) {
        int region = Math.abs(key.hashCode() % _subregionCnt);
        synchronized (_subregions[region]) {
            return _subregions[region].get(key);
        }
    }

    public void flush() {
        for (int cnt = 0; cnt < _subregionCnt; cnt++) {
            synchronized (_subregions[cnt]) {
                _subregions[cnt].clear();
            }
        }
    }    
        /*
       * the lru is never full in the worst case an element is dropped
       * */

    public boolean full() {
        return false;
    }

    public void put(String key, Object data) {
        int region = Math.abs(key.hashCode() % _subregionCnt);
        synchronized (_subregions[region]) {
            _subregions[region].put(key, data);
        }
    }
}