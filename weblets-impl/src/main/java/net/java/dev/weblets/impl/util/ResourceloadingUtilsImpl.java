package net.java.dev.weblets.impl.util;

import net.java.dev.weblets.impl.servlets.WebletRequestImpl;
import net.java.dev.weblets.packaged.IResourceloadingUtils;
import net.java.dev.weblets.util.VersioningUtils;
import net.java.dev.weblets.util.CopyStrategy;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.resource.CachingSubbundleResourceImpl;
import net.java.dev.weblets.resource.Subbundle;
import net.java.dev.weblets.resource.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author werpu
 * @date: 14.11.2008
 */
public class ResourceloadingUtilsImpl implements IResourceloadingUtils {

    static final String TEMP_CACHE_KEY = "TEMP_CACHE";

    public static final int CACHED_URLS = 3000;
    public static final String CACHE_KEY = "WEBLET_CACHE";

    VersioningUtils versioningUtils = new VersioningUtils();
    String tempDir = "";

    public ResourceloadingUtilsImpl() {
    }

    public URL getResourceUrl(WebletRequest request, String resourcePath) {
        Map urlCache = getResourceURLCache(request);
        URL url = null;
        url = (URL) urlCache.get(resourcePath);
        if (url != null)
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
     * Get resource for a url construct mapping into our new WebletResource
     * Class
     *
     * @param request      the weblet request
     * @param resourcePath the full resource path
     * @return a WebletResource object describing the raw resource!
     * @throws java.io.IOException
     */
    public WebletResource getResource(WebletConfig config, WebletRequest request, String resourcePath) throws IOException {
        URL resourceURL = getResourceUrl(request, resourcePath);
        return new CachingURLResourceImpl(config, request, resourceURL);
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

    public WebletRequest createWebletRequest(String webletName, String webletPath, String contextPath, String webletPathInfo,long ifModifiedSince, Object httpRequest) {
        WebletRequest webRequest = new WebletRequestImpl(webletName, webletPath, contextPath, webletPathInfo, ifModifiedSince, httpRequest);
        return webRequest;
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
    }

    /**
     * loads a resource from a given url
     *
     * @param config       the current weblet config
     * @param request      the current weblet request
     * @param response     the current weblet response
     * @param url          the current url
     * @param copyProvider the processing filter chain for the weblet serving
     * @deprecated
     */
    public void loadFromUrl(WebletConfig config, WebletRequest request, WebletResponse response, URL url, CopyStrategy copyProvider) throws IOException {
        //TODO check if the request is a bundle request and if yes trigger the bundeling subsystem instead of
        //the simple url serving subsystem
        if (url == null) {
            response.setStatus(WebletResponse.SC_NOT_FOUND);
            return;
        }
        //if (!hasSubbundles(config)) {     Subbundle handling only in the new cleaned up methods!
        loadSimpleResource(config, request, response, url, copyProvider);
        /*} else {
            Subbundle bundle = getResourceBundle(config, request);
            if (bundle == null) {
                loadSimpleResource(config, request, response, url, copyProvider);
                return;
            }
        }*/
    }
    /*
    lade resource:

    konvertiere den request in eine resource  -> factory, subbundles



    lade die resource anhand der daten -> copyprovider anwerfen, notfalls konkatenieren!


    */

    /**
     * Central entry point for the resource loading
     * loads a resource from the given parameters
     *
     * @param config           the weblet config
     * @param request          the weblet request
     * @param response         the weblet response
     * @param resourceResolver our resource resolver which maps a valid request into a resource depending on the state of the application
     * @param copyStrategy     the copy strategy which does the preprocessing of resources
     * @throws IOException in case of an error!
     *                     <p/>
     *                     TODO add a raw copy to the strategy for cases where we already work on preprocessed streams!
     */
    public void loadResource(WebletConfig config, WebletRequest request, WebletResponse response, ResourceResolver resourceResolver, CopyStrategy copyStrategy) throws IOException {
        WebletResource resource = getResourceFactory(config).getResource(request, resourceResolver, true);   /*we cache but for now now temp file*/

        if (resource == null) {
            response.setStatus(WebletResponse.SC_NOT_FOUND);
            return;
        } else if (resource != null) {
            if (resource instanceof CachingSubbundleResourceImpl) {
                //we have a bundle, we have to prepare the files if needed!
                CachingSubbundleResourceImpl bundle = (CachingSubbundleResourceImpl) resource;
                preprocessSubbundleResource(request, copyStrategy, bundle);
            } else {
                preprocessResource(request, copyStrategy, resource);
            }
            loadResourceFromStream(config, request, response, copyStrategy, resource.getInputStream(), resource.lastModified());
        }
    }

    long _tmpCnt = 0;

    /**
     * Preprocesses
     *
     * @param request
     * @param copyStrategy
     * @param bundle
     * @throws IOException
     */
    private void preprocessSubbundleResource(WebletRequest request, CopyStrategy copyStrategy, CachingSubbundleResourceImpl bundle) throws IOException {
        if (bundle.isRecreateTemp()) {
            synchronized (this) {//all stop we do not want any further threads doing anything until we have the resource temps in place again!
                if (!bundle.isRecreateTemp()) {//lets check if another thread has recreated already
                    return;
                }
                createTempFile(request, bundle);
                Iterator it = bundle.getSubresources().iterator();
                File outFile = bundle.getTemp();
                outFile.createNewFile();
                //concatenation
                int cnt = 0;
                while (it.hasNext()) {
                    WebletResource subResource = (WebletResource) it.next();
                    preprocessResource(request, copyStrategy, subResource);
                    FileOutputStream ostr = new FileOutputStream(outFile, true);
                    copyStrategy.copy(request.getWebletName(), subResource.getMimetype(), subResource.getInputStream(), ostr);
                }
                //closing is done automatically by the copy strategy
                bundle.setRecreateTemp(false);
            }
        }
    }

    private void createTempFile(WebletRequest request, WebletResource resource) throws IOException {
        File tempFile = File.createTempFile(request.getWebletName().replaceAll("\\.", "_") + "_" + request.getWebletPath().replaceAll("\\/", "_").replaceAll("\\\\", "_") + (_tmpCnt++), "weblet");
        resource.setTemp(tempFile);
    }

    /**
     * Resource preprocessing
     * this is the preprocessing part
     * which preprocesses resources
     * into temp files and later on those
     * files are streamed from the temp files
     * into the request
     *
     * @param request      our weblet request
     * @param copyStrategy the copy strategy to be applied onto the resource
     * @param resource     the resource to be processed by the copy strategy
     * @throws IOException
     */
    private void preprocessResource(WebletRequest request, CopyStrategy copyStrategy, WebletResource resource) throws IOException {
        if ((!(resource instanceof CachingSubbundleResourceImpl)) && resource.isProcessTemp() && resource.isRecreateTemp()) {//TODO a better temp recreateion determination
            synchronized (resource) {
                if (!resource.isRecreateTemp()) {
                    return;
                }
                createTempFile(request, resource);
                File subOutFile = resource.getTemp();
                FileOutputStream subOstr = new FileOutputStream(subOutFile);
                InputStream subInputStream = resource.getUnprocessedInputStream();
                if (resource instanceof Subbundle) {
                    //no further processing the subbundles already have been processed
                    //the mime type is set to the response on the outside!
                    copyStrategy.copy(request.getWebletName(), "application/octed-stream", subInputStream, subOstr);
                } else {
                    copyStrategy.copy(request.getWebletName(), resource.getMimetype(), subInputStream, subOstr);
                }
            }
        }
    }

    private void logError(IOException ex) {
        Log log = LogFactory.getLog(getClass());
        log.error(ex);
    }

    private void loadSimpleResource(WebletConfig config, WebletRequest request, WebletResponse response, URL url, CopyStrategy copyProvider) throws IOException {
        URLConnection conn = url.openConnection();
        long lastmodified = conn.getLastModified();
        loadResourceFromStream(config, request, response, copyProvider, conn.getInputStream(), lastmodified);
    }

    /**
     * sets initial response params upon the version state
     *
     * @param config       the weblet config
     * @param response     the weblet response
     * @param lastmodified local resource lastmodified
     */
    private void prepareVersionedResponse(WebletConfig config, WebletResponse response, long lastmodified, long timeout) {
        String webletVersion = config.getWebletVersion();
        if (!VersioningUtils.isVersionedWeblet(webletVersion)) {
            response.setLastModified(lastmodified);
            /*
                * set the expires and content version in the head
                */
            response.setContentVersion((webletVersion == null) ? "" : webletVersion, VersioningUtils.getPast());
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

    /*loads a bundled resource!*/
    private void loadBundledResource(WebletConfig config, WebletRequest request, WebletResponse responce, Subbundle bundle, CopyStrategy copyProvider, long resourceLastmodified) {
        //first check the dates if the are already reserved
        //URL url = ResourceloadingUtilsImpl.getInstance().getResourceUrl(request, resourcePath);
    }

    /**
     * loads a given resource from an input stream it uses internal timestamps for resource handling and resource serving this works on most browser but safari
     * seems to ignore the timestamps and always sends a modifiedSince for resources for 1.1.1970
     *
     * @param config               the weblets config for this resource loading request
     * @param request              the weblets request
     * @param response             the weblets response
     * @param copyProvider         a given processing copy provider
     * @param in                   the input stream for the processing
     * @param resourceLastmodified the lastmodified for the given input stream
     * @throws IOException in case of an internal processing error
     */
    public void loadResourceFromStream(WebletConfig config, WebletRequest request, WebletResponse response, CopyStrategy copyProvider, InputStream in,
                                       long resourceLastmodified) throws IOException {
        if (in != null) {
            // mime-type
            if (versioningUtils.hasTobeLoaded(config, request, resourceLastmodified, this)) {
                prepareVersionedResponse(config, response, resourceLastmodified, System.currentTimeMillis() + versioningUtils.getTimeout(config, this));
                //response.setContentType(finalMimetype);
                loadResourceFromStream(config, request, response, copyProvider, in);
                // response.setStatus(200);
            } else {
                /* we have to set the timestamps as well here */
                prepareVersionedResponse(config, response, resourceLastmodified, request.getIfModifiedSince()
                                                                                 + TimeZone.getDefault().getOffset(request.getIfModifiedSince()));
                //response.setContentType(null); // Bogus "text/html" overriding
                response.setStatus(WebletResponse.SC_NOT_MODIFIED);
            }
        } else {
            response.setStatus(WebletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * loads the resource from a given input stream note,
     * this api is under construction we have caching not enabled yet
     *
     * @param config       the weblet config to load the resource
     * @param request      the weblet request
     * @param response     the weblet response
     * @param copyProvider the processing copy provider
     * @param in           the resource serving input stream
     * @throws IOException
     */
    public void loadResourceFromStream(WebletConfig config, WebletRequest request, WebletResponse response, CopyStrategy copyProvider, InputStream in)
            throws IOException {
        OutputStream out = response.getOutputStream();
        String finalMimetype = config.getMimeType(request.getPathInfo());
        copyProvider.copy(request.getWebletName(), finalMimetype, in, out);
    }

    public ResourceFactory getResourceFactory(WebletConfig config) {
        ResourceFactory cache = (ResourceFactory) config.getConfigParam(TEMP_CACHE_KEY);
        if (cache == null) {
            synchronized (config) {
                cache = (ResourceFactory) config.getConfigParam(TEMP_CACHE_KEY);
                if (cache != null) {
                    return cache;
                }
                cache = new CachedResourceFactory(config);
                config.setConfigParam(TEMP_CACHE_KEY, cache);
            }
        }
        return cache;
    }
}
