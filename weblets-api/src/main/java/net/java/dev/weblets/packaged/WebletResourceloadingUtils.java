package net.java.dev.weblets.packaged;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.util.CopyStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import net.java.dev.weblets.util.StringUtils;
import net.java.dev.weblets.util.CopyStrategy;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 * helper class to be shared by various weblet loaders
 * 
 * @author Werner Punz
 */
public class WebletResourceloadingUtils {
	static WebletResourceloadingUtils	instance		= new WebletResourceloadingUtils();
	private static final long			MILLIS_PER_YEAR	= 1000l * 60l * 60l * 24l * 365l;
	static final int					CACHED_URLS		= 3000;
	static final String					CACHE_KEY		= "WEBLET_CACHE";

	public URL getResourceUrl(WebletRequest request, String resourcePath) {
		Map urlCache = getResourceURLCache(request);
		URL url = null;
		
		url = (URL) urlCache.get(resourcePath);
		if(url != null)
			return url;
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		url = loader.getResource(resourcePath);
		if (url == null) {
			loader = getClass().getClassLoader();
			url = loader.getResource(resourcePath);
		}
		urlCache.put(resourcePath, url);
		return url;
	}

	/**
	 * fetches the resource url from a given resource path (uncached for reporting only) TODO add a better cache (LRU if possible)
	 * 
	 * @param resourcePath
	 * @return
	 */
	public URL getResourceUrl(String resourcePath) {
		URL url = null;
		if (url != null)
			return url;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		url = loader.getResource(resourcePath);
		if (url == null) {
			loader = getClass().getClassLoader();
			url = loader.getResource(resourcePath);
		}
		return url;
	}

	/* entry cache per session */
	private Map getResourceURLCache(WebletRequest request) {
		HttpSession session = ((HttpServletRequest) request.getExternalRequest()).getSession();
		// session
		Map cache = (Map) session.getAttribute(CACHE_KEY);
		if (cache == null) {
			cache = Collections.synchronizedMap(new HashMap(CACHED_URLS));
			session.setAttribute(CACHE_KEY, cache);
		}
		if (cache.size() >= CACHED_URLS) {
			cache.clear();
		}
		return cache;
	};

	public static WebletResourceloadingUtils getInstance() {
		return instance;
	}

	/**
	 * loads a resource from a given url
	 * 
	 * @param config
	 *            the current weblet config
	 * @param request
	 *            the current weblet request
	 * @param response
	 *            the current weblet response
	 * @param url
	 *            the current url
	 * @param copyProvider
	 *            the processing filter chain for the weblet serving
	 */
	public void loadFromUrl(WebletConfig config, WebletRequest request, WebletResponse response, URL url, CopyStrategy copyProvider) throws IOException {
		if (url != null) {
			URLConnection conn = url.openConnection();
			long lastmodified = conn.getLastModified();
			loadResourceFromStream(config, request, response, copyProvider, conn.getInputStream(), lastmodified);
		} else {
			response.setStatus(WebletResponse.SC_NOT_FOUND);
		}
	}

	/**
	 * sets initial response params upon the version state
	 * 
	 * @param config
	 *            the weblet config
	 * @param response
	 *            the weblet response
	 * @param lastmodified
	 *            local resource lastmodified
	 */
	private void prepareVersionedResponse(WebletConfig config, WebletResponse response, long lastmodified, long timeout) {
		String webletVersion = config.getWebletVersion();
		if (!isVersionedWeblet(webletVersion)) {
			response.setLastModified(lastmodified);
			/*
			 * set the expires and content version in the head
			 */
			response.setContentVersion((webletVersion == null) ? "" : webletVersion, getPast());
		} else {
			// we lock out resource loading once versioned, we do not run
			// into the chain
			// just in case the expires is ignored
			// this enforces the loading from cache on some browsers
			// even if refresh is pressed, this is by
			// definition the wanted behavior if versioning is on!
			// some browsers like firefox despite
			// having a future number pass a local date on refresh maybe we
			// lock this out as well
			response.setLastModified(timeout);
			// this should prevent requests entirely!
			response.setContentVersion(webletVersion, timeout);
		}
	}

	private long getTimeout(WebletConfig config) {
		String cacheControlTimeout = config.getInitParameter("cachecontrol-timeout");
		long timeout = WebletResourceloadingUtils.getNever();
		if (!StringUtils.isBlank(cacheControlTimeout)) {
			try {
				timeout = Long.parseLong(cacheControlTimeout);
			} catch (RuntimeException ex) {
				Log log = LogFactory.getLog(this.getClass());
				log.error("Weblets: Cache control is set but to an invalid value setting now never instead");
			}
		}
		return timeout;
	}

	/**
	 * fixes internal time values browsers deliver time values on seconds internally it is calculated in miliseconds
	 * 
	 * @param browserTimeValue
	 *            the browser time value
	 * @return returns a fixed second time value for the input
	 */
	private long fixTimeValue(long browserTimeValue) {
		// some browsers only work on seconds (Mozilla) so we go down to one
		// second for a shared
		// common response time
		// we cannot tamper the cache state here, because
		// otherwise firefox will fail with an emptied page resource cache
		// (shift f5)
		if (browserTimeValue > 1000)
			browserTimeValue = browserTimeValue - browserTimeValue % 1000;
		return browserTimeValue;
	}

	/**
	 * loads a given resource from an input stream it uses internal timestamps for resource handling and resource serving this works on most browser but safari
	 * seems to ignore the timestamps and always sends a modifiedSince for resources for 1.1.1970
	 * 
	 * @param config
	 *            the weblets config for this resource loading request
	 * @param request
	 *            the weblets request
	 * @param response
	 *            the weblets response
	 * @param copyProvider
	 *            a given processing copy provider
	 * @param in
	 *            the input stream for the processing
	 * @param resourceLastmodified
	 *            the lastmodified for the given input stream
	 * @throws IOException
	 *             in case of an internal processing error
	 */
	public void loadResourceFromStream(WebletConfig config, WebletRequest request, WebletResponse response, CopyStrategy copyProvider, InputStream in,
			long resourceLastmodified) throws IOException {
		if (in != null) {
			// mime-type
			long requestCacheState = request.getIfModifiedSince();
			// the browser sends the utc timestamp
			requestCacheState = fixTimeValue(requestCacheState);
			long resourceModifiedState = resourceLastmodified;
			resourceModifiedState = fixTimeValue(resourceModifiedState);
			boolean load = false;
			long currentTime = System.currentTimeMillis();
			// utc time mapping
			long currentUTCTime = currentTime - TimeZone.getDefault().getOffset(currentTime);
			long utcResourceModifiedState = (resourceModifiedState - TimeZone.getDefault().getOffset(resourceModifiedState)) + getTimeout(config);
			load = (requestCacheState < utcResourceModifiedState)
			/*-1 or smaller value on reload pressed*/
			|| requestCacheState < currentUTCTime;
			/* cache control timeout reached we reload no matter what! */
	

			if (load) {
				prepareVersionedResponse(config, response, resourceLastmodified, System.currentTimeMillis() + getTimeout(config));
				//response.setContentType(finalMimetype);
				
				loadResourceFromStream(config, request, response, copyProvider, in);
				// response.setStatus(200);
			} else {
				/* we have to set the timestamps as well here */
				prepareVersionedResponse(config, response, resourceLastmodified, request.getIfModifiedSince()
						+ TimeZone.getDefault().getOffset(request.getIfModifiedSince()));
				response.setContentType(null); // Bogus "text/html" overriding
				response.setStatus(WebletResponse.SC_NOT_MODIFIED);
			}
		} else {
			response.setStatus(WebletResponse.SC_NOT_FOUND);
		}
	}

	/**
	 * loads the resource from a given input stream note, this api is under construction we have caching not enabled yet
	 * 
	 * @param config
	 *            the weblet config to load the resource
	 * @param request
	 *            the weblet request
	 * @param response
	 *            the weblet response
	 * @param copyProvider
	 *            the processing copy provider
	 * @param in
	 *            the resource serving input stream
	 * @throws IOException
	 */
	public void loadResourceFromStream(WebletConfig config, WebletRequest request, WebletResponse response, CopyStrategy copyProvider, InputStream in)
			throws IOException {
		OutputStream out = response.getOutputStream();
		String finalMimetype = config.getMimeType(request.getPathInfo());
		if(StringUtils.isBlank(finalMimetype)) {
			finalMimetype = response.getDefaultContentType();
		}
		response.setContentType(finalMimetype);
		copyProvider.copy(request.getWebletName(), finalMimetype, in, out);
	}

	/* unified version checker for weblet versions maybe in existence */
	public static boolean isVersionedWeblet(String webletVersion) {
		return webletVersion != null && !webletVersion.trim().equals("") && !webletVersion.endsWith("-SNAPSHOT");
	}

	/* defined never value used system internally */
	public static long getNever() {
		long now = System.currentTimeMillis();
		return now + MILLIS_PER_YEAR;
	}

	public static long getPast() {
		long now = System.currentTimeMillis();
		return now - MILLIS_PER_YEAR;
	}
}
