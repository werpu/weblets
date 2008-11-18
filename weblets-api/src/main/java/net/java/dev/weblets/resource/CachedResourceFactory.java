package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.resource.SimpleCachingProvider;
import net.java.dev.weblets.resource.Cache;
import net.java.dev.weblets.resource.Subbundle;
import net.java.dev.weblets.resource.ResourceResolver;
import net.java.dev.weblets.resource.WebletResource;
import net.java.dev.weblets.resource.ResourceFactory;

import java.io.IOException;
import java.util.*;

/**
 * Class doing the temp
 * handling
 */
public class CachedResourceFactory implements ResourceFactory {

    WebletConfig _config;

    Cache _tempfileCache = SimpleCachingProvider.getInstance().getCache("tempfileCache");

    boolean _cacheFile = true;

    int _tmpCnt = 0;

    public CachedResourceFactory(WebletConfig config) {
        _config = config;
    }

    /**
     * Scratchpat
     * <p/>
     * Request comes in
     * Bundle is checked for a list of files
     * if list of files is found
     * <p/>
     * Each of those files is processed and the results being put into a temp
     * dir (if not present)
     * <p/>
     * then the files are concatenated and the result also is being put into a temp dir
     * <p/>
     * <p/>
     * Processing of a single file
     * determine temp file name
     * <p/>
     * check if the temp file name exists in the temp dir
     * if no then create the file stream the output in
     * and return a file handle for the temp dir
     * <p/>
     * if filename exists and timestamp is older than the timestamp of the new file resource
     * then create a new temp file with (with a number to get a clear handle on the resource
     * for a correct sort order)
     * <p/>
     * and proceed as bevore.
     * <p/>
     * if the timestamp is newer or equals than the one of the original resource dont do the processing
     * and return simply the temp file!
     * <p/>
     * Synchronization:
     * while the temp files are processed in a write manner sync locks have to be performed if something
     * accesses the same resource!
     */

    public WebletResource getResource(WebletRequest request, ResourceResolver resourceResolver, boolean resolveSubbundle) throws IOException {
        WebletResource webletResource = null;
        //ie js + compression means temp true
        //plain png means temp false!
        if (_cacheFile) {
            webletResource = (WebletResource) _tempfileCache.get(getWebletKey(request));
        }
        //no temp file found we create one
        Subbundle bundle = null;
        if (resolveSubbundle) {
            bundle = getResourceBundle(_config, request);
        }
        if (webletResource == null && bundle != null) {
            /*Resource is prepared as bundle resource and then returned*/
            webletResource = new SubbundleResourceImpl(_config, request, resourceResolver, bundle);
            handleNewFile(request, resourceResolver, webletResource);
            return webletResource;
        }
        //non bundle case!
        if (webletResource == null) {  //new resource
            webletResource = resourceResolver.getResource(request);
            if (webletResource == null) {
                return null;
            }
            //no temporary file creation activation
            if (!webletResource.isProcessTemp()) {
                return webletResource;
            }
            handleNewFile(request, resourceResolver, webletResource);
        } else if (webletResource.isProcessTemp() && webletResource.tempLastmodified() < webletResource.lastModified()) { //timestamps have changed
            handleNewFile(request, resourceResolver, webletResource);
        }
        return webletResource;
    }

    /**
     * fetches the proper subbundle from the given webles request
     *
     * @param config  the config of the current weblt
     * @param request a valid weblets request
     * @return a valid subbundle or null if none is found!
     */
    private Subbundle getResourceBundle(WebletConfig config, WebletRequest request) {
        String resource = request.getPathInfo();
        Subbundle bundle = config.getBundleFromId(resource);
        if (bundle != null) return bundle;
        bundle = config.getBundleFromResources(resource);
        return bundle;
    }

    /**
     * checks if the current config has a valid subbundle
     *
     * @param config
     * @return
     */
    private boolean hasSubbundles(WebletConfig config) {
        Collection subbundles = config.getSubbundles();
        return subbundles != null && subbundles.size() > 0;
    }

    /**
     * handles a new file with a temped
     * entry if caching is needed!
     *
     * @param request
     * @param resourceResolver
     * @param webletResource
     * @throws IOException
     */
    private void handleNewFile(WebletRequest request, ResourceResolver resourceResolver, WebletResource webletResource) throws IOException {
        //TODO can be problematic under heavy load!!!!
        //we use an atomic int cnt to avoid that one request accesses a temp file while another does the file recreation!
        if (_cacheFile) {
            _tempfileCache.put(getWebletKey(request), webletResource);
        }
        webletResource.setRecreateTemp(true);
    }

    //TODO set one cache per weblet instead of one central cache in the long run!
    private String getWebletKey(WebletRequest request) {
        return request.getWebletName() + "/weblet/" + request.getPathInfo();
    }
}
  