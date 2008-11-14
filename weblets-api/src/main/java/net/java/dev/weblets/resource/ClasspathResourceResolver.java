package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.packaged.ResourceloadingUtils;
import net.java.dev.weblets.resource.ResourceResolver;
import net.java.dev.weblets.resource.URLResourceImpl;
import net.java.dev.weblets.resource.WebletResource;

import java.io.IOException;
import java.net.URL;

/**
 * @author werpu
 * @date: 04.11.2008
 * <p/>
 * A URL resolver implementation
 * for the current classpath to fetch a valid resource!
 */
public class ClasspathResourceResolver implements ResourceResolver {

    WebletConfig _config;
    String _resourceRoot;

    public ClasspathResourceResolver(WebletConfig config, String resourceRoot) {
        _config = config;
        _resourceRoot = resourceRoot;
    }

    /**
     * Central call method for every url resolver
     * it maps a valid request into an internal url for further processing!
     *
     * @param request the incoming request
     * @return a valid url pointing to a resource or shadow resource!
     * @throws IOException in case of an error!
     */
    public URL getURL(WebletRequest request) throws IOException {
        String resourcePath = _resourceRoot + request.getPathInfo();
        return ResourceloadingUtils.getInstance().getResourceUrl(request, resourcePath);
    }

    /**
     * Extension point for reporting
     * it omits the WebletRequest constructs for now!
     *
     * @param pathInfo the pathinfo of the resource
     * @return a valid url
     * @throws IOException in case of an error
     */
    public URL getURL(String mimetype, String pathInfo) throws IOException {
        String resourcePath = _resourceRoot + pathInfo;
        URL url = ResourceloadingUtils.getInstance().getResourceUrl(resourcePath);
        return url;
    }

    public WebletResource getResource(WebletRequest request) throws IOException {
        URL url = getURL(request);
        if (url == null) {
            return null;
        }
        WebletResource retVal = new URLResourceImpl(_config, request, url);
        return retVal;
    }

    public WebletResource getResource(String mimetype, String pathInfo) throws IOException {
        URL url = getURL(mimetype, pathInfo);
        if (url == null) {
            return null;
        }
        WebletResource retVal = new URLResourceImpl(_config, mimetype, url);
        return retVal;
    }

    ;
}
