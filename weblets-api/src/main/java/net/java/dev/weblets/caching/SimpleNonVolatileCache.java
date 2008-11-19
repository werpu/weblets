package net.java.dev.weblets.caching;

import net.java.dev.weblets.caching.Cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author werpu
 * @date: 18.11.2008
 * <p/>
 * Non volatile cache for storing certain non volatile
 * data, like resource references etc...
 * <p/>
 * the cache has a low synchronziation code
 * percentage, to synchronize only on the changing parts
 * to make read access as fast as possible!
 */
public class SimpleNonVolatileCache implements Cache {

    /**
     * we split the cache into 10 internal regions to speed things up
     */
    Map[] _subregions = new Map[SUB_REGIONS];
    int _capacity = -1;
    private static final int SUB_REGIONS = 10;
    int _estimatedSize = 0;

    public SimpleNonVolatileCache() {
        for (int cnt = 0; cnt < SUB_REGIONS; cnt++) {
            _subregions[cnt] = new HashMap();
        }
    }

    public SimpleNonVolatileCache(int capacity) {
        _capacity = capacity;
        for (int cnt = 0; cnt < SUB_REGIONS; cnt++) {
            _subregions[cnt] = new HashMap();
        }
    }

    public Object get(String key) {
        int region = Math.abs(key.hashCode() % SUB_REGIONS);
        synchronized (_subregions[region]) {
            return _subregions[region].get(key);
        }
    }

    public void flush() {
        for (int cnt = 0; cnt < SUB_REGIONS; cnt++) {
            synchronized ( _subregions[cnt]) {
                _subregions[cnt].clear();/*clear is way slower than replacing*/
                _estimatedSize = 0;
            }
        }
    }

    public boolean full() {
        if (_capacity == -1) {
            return false;
        }
        return _estimatedSize >= _capacity;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void put(String key, Object data) {
        int region = Math.abs(key.hashCode() % SUB_REGIONS);
        synchronized (_subregions[region]) {
            _subregions[region].put(key, data);
            _estimatedSize++;
        }
    }
}
