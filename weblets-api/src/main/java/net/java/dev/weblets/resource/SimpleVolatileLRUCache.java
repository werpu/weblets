package net.java.dev.weblets.resource;

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
    Map[] _delegates = null;
    int _capacity;
    private int _subregions = 20;

    private static int DIST_WEIGHT = 10; //distribution weight also the number of parallel threads per access

    public SimpleVolatileLRUCache(int capacity) {
        _capacity = capacity;
        _subregions = Math.max(3, capacity / DIST_WEIGHT);  //at least 3 lru regions!
        _delegates = new Map[_subregions];
        for (int cnt = 0; cnt < _subregions; cnt++) {
            _delegates[cnt] = new LinkedHashMap(1, 0.75f, true) {
                /*put already under synchronized remove should not do anything wild*/
                protected boolean removeEldestEntry(final Map.Entry eldest) {
                    return size() > _capacity;
                }
            };
        }
    }

    public Object get(String key) {
        int region = Math.abs(key.hashCode() % _subregions);
        synchronized (_delegates[region]) {
            return _delegates[region].get(key);
        }
    }

    public void flush() {
        for (int cnt = 0; cnt < _subregions; cnt++) {
            synchronized (_delegates[cnt]) {
                _delegates[cnt].clear();
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
        int region = Math.abs(key.hashCode() % _subregions);
        synchronized (_delegates[region]) {
            _delegates[region].put(key, data);
        }
    }
}