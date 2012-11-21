package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.resource.WebletResource;

import java.io.IOException;
import java.net.URL;

/**
 * @author werpu
 * @date: 04.11.2008
 * <p/>
 * A generic url resolver
 * which resolves a valid request
 * into a url depending on
 * the weblet and state of the application
 */
public interface ResourceResolver {
    /**
     * Central call method for every url resolver
     * it maps a valid request into an internal url for further processing!
     *
     * @param request the incoming request
     * @return a valid url pointing to a resource or shadow resource!
     * @throws IOException in case of an error!
     * @deprecated
     */
    URL getURL(WebletRequest request) throws IOException;

    /**
     * Extension point for reporting
     * it omits the WebletRequest constructs for now!
     *
     * @param pathInfo the pathinfo of the resource
     * @return a valid url
     * @throws IOException in case of an error
     * @deprecated
     */
    public URL getURL(String pathInfo) throws IOException;

    /**
     * returns a resource upon a given request and mimetype
     *
     * @param request
     * @return a resource if the resource itself can be determined<b> null if none can be found </b>
     * @throws IOException
     */
    public WebletResource getResource(WebletRequest request) throws IOException;

    /**
     * returns a resource upon a given path and mimetype!
     *
     * @param mimetype
     * @param pathInfo
     * @return
     * @throws IOException
     */
    public WebletResource getResource(String mimetype, String pathInfo) throws IOException;
}
