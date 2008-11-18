package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.resource.ResourceResolver;
import net.java.dev.weblets.resource.WebletResource;
import net.java.dev.weblets.resource.URLResourceImpl;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.io.IOException;

/**
 * @author werpu
 * @date: 04.11.2008
 * <p/>
 * A generic url resolver object
 * to resolve our weblet request objects
 * into valid urls!
 */
public class WebappResourceResolver implements ResourceResolver {

    WebletConfig _config;

    String _resourceRoot;

    public WebappResourceResolver(WebletConfig config, String resourceRoot) {
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
        HttpServletRequest httpRequest = (HttpServletRequest) request.getExternalRequest();
        URL url = httpRequest.getSession().getServletContext().getResource("/" + resourcePath);
        System.out.println(url.openConnection().getLastModified());
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
        //not implemented yet
        return null;
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
        //not implemented yet
        return null;
    }
}
