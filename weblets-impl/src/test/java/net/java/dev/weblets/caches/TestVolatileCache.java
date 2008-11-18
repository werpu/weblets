package net.java.dev.weblets.caches;

import junit.framework.TestCase;
import net.java.dev.weblets.resource.Cache;
import net.java.dev.weblets.resource.SimpleVolatileLRUCache;
import net.java.dev.weblets.resource.NonVolatileCache;

/**
 * @author werpu
 * @date: 18.11.2008
 */
public class TestVolatileCache extends TestCase {

    static int threadCnt = 0;

    class TestRunner implements Runnable {
        Cache _cache = null;

        public TestRunner( Cache cache) {
      
            _cache = cache;
        }

        public void run() {
            threadCnt++;
            try {
                for (int cnt = 0; cnt < 100; cnt++) {
                    _cache.put(cnt + "", "hello world");
                    
                     
                        if (cnt % 67 == 0) {
                            _cache.flush();
                        }

                    _cache.get(cnt + "");
                }
            } catch (Exception e) {
               fail(e.toString());
            } finally {
                threadCnt--;
            }
        }
    }

    /**
     *
     */
    public void testVolatileCache() {
        threadCnt = 0;
        Cache cache = new SimpleVolatileLRUCache(700);
         ThreadGroup g =  new ThreadGroup("grp1");
        for (int cnt = 0; cnt < 100; cnt++) {
            Runnable runner = new TestRunner(cache);
            Thread t = new Thread (g, runner);
            t.start();
        }
        while (threadCnt > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                super.fail(e.toString());
            }
        }
    }

    public void testNonVolatileCache() {
        threadCnt = 0;
        Cache cache = new NonVolatileCache();
        ThreadGroup g =  new ThreadGroup("grp2");
        for (int cnt = 0; cnt < 100; cnt++) {
              Runnable runner = new TestRunner(cache);
            Thread t = new Thread (g, runner);
            t.start();
        }
        while (threadCnt > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                super.fail(e.toString());
            }
        }
    }   
}
