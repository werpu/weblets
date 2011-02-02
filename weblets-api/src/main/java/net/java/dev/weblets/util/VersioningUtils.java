package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.packaged.ResourceloadingUtils;
import net.java.dev.weblets.packaged.IResourceloadingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.TimeZone;

/**
 * @author werpu
 * @date: 05.11.2008
 * <p/>
 * All the versioning processing
 * in its own utils class!
 */
public class VersioningUtils {
    public static final long MILLIS_PER_YEAR = 1000l * 60l * 60l * 24l * 365l;
    private static final String MARKER_SNAPSHOT = "-SNAPSHOT";
    private static final String ERR_INVALID_CACHECTRL_VALUE = "Weblets: Cache control is set but to an invalid value setting now never instead";
    private static final int BROWSER_FUZZYFICATION = 1000;
    private static final String PAR_CACHECONTROL_TIMEOUT = "cachecontrol-timeout";

    /**
     * fetches the timeout value from the given config instance!
     *
     * @param config               the weblet config
     * @param resourceloadingUtils
     * @return a timeout if one is found otherwise a never value is returned marking a point in the far future
     */
    public long getTimeout(WebletConfig config, IResourceloadingUtils resourceloadingUtils) {
        String cacheControlTimeout = config.getInitParameter(PAR_CACHECONTROL_TIMEOUT);
        long timeout = getNever();
        if (!StringUtils.isBlank(cacheControlTimeout)) {
            try {
                timeout = Long.parseLong(cacheControlTimeout);
            } catch (RuntimeException ex) {
                Log log = LogFactory.getLog(resourceloadingUtils.getClass());
                log.error(ERR_INVALID_CACHECTRL_VALUE);
            }
        }
        return timeout;
    }

    /**
     * fixes internal time values browsers deliver time
     * values on seconds internally it is calculated
     * in miliseconds
     *
     * @param browserTimeValue the browser time value
     * @return returns a fixed second time value for the input
     */
    public long fixTimeValue(long browserTimeValue) {
        // some browsers only work on seconds (Mozilla) so we go down to one
        // second for a shared
        // common response time
        // we cannot tamper the cache state here, because
        // otherwise firefox will fail with an emptied page resource cache
        // (shift f5)
        if (browserTimeValue > BROWSER_FUZZYFICATION)
            browserTimeValue = browserTimeValue - browserTimeValue % BROWSER_FUZZYFICATION;
        return browserTimeValue;
    }

    /* unified version checker for weblet versions maybe in existence */
    public static boolean isVersionedWeblet(String webletVersion) {
        return webletVersion != null && !webletVersion.trim().equals("") && !webletVersion.endsWith(MARKER_SNAPSHOT);
    }/* defined never value used system internally */

    public static long getNever() {
        long now = System.currentTimeMillis();
        return now + MILLIS_PER_YEAR;
    }

    public static long getPast() {
        long now = System.currentTimeMillis();
        return now - MILLIS_PER_YEAR;
    }

    /**
     * check if the resource has to be loaded
     * upon the given environmental and time parameters!
     * <p/>
     * the calculation is done internally as utc values and the current timezone
     * shifting being done internally correctly to those utc values!
     *
     * @param config               the current weblets config
     * @param request              the weblets request
     * @param resourceLastmodified the last modified state of the resource
     * @param resourceloadingUtils
     * @return true if a reload has to be done false if not
     */
    public boolean hasTobeLoaded(WebletConfig config, WebletRequest request, long resourceLastmodified, IResourceloadingUtils resourceloadingUtils) {
        boolean load = false;
         String webletVersion = config.getWebletVersion();
        if (isVersionedWeblet(webletVersion)) {
            //we check if the resource last modified + timeout < currentTime, thats the only condition we cannot
            //Serve a resource served, we have a strong
            long requestCacheState = request.getIfModifiedSince();
            // the browser sends the utc timestamp
            requestCacheState = fixTimeValue(requestCacheState);
            long currentTime = System.currentTimeMillis();
            // utc time mapping
            long currentUTCTime = currentTime - TimeZone.getDefault().getOffset(currentTime);
            currentUTCTime = fixTimeValue(currentUTCTime);

            load = requestCacheState != -1 && (requestCacheState + getTimeout(config, resourceloadingUtils)) < currentUTCTime;
        } else {
            long requestCacheState = request.getIfModifiedSince();
            resourceLastmodified = fixTimeValue(resourceLastmodified);
            long utcResourceModifiedState = (resourceLastmodified - TimeZone.getDefault().getOffset(resourceLastmodified));
            load = requestCacheState < utcResourceModifiedState;
        }
        return load;
    }
}
