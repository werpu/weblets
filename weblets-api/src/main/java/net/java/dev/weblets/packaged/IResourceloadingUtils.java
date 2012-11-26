package net.java.dev.weblets.packaged;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.resource.ResourceResolver;
import net.java.dev.weblets.resource.ResourceFactory;
import net.java.dev.weblets.util.CopyStrategy;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author werpu
 * @date: 14.11.2008
 * <p/>
 * Public interfaces exposing
 * the resource loading utils implementation
 * to the outside world
 * <p/>
 * This is the central resource loading
 * handler triggering all subsystems
 * every resource call has to pass this class
 */
public interface IResourceloadingUtils {

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
    public void loadFromUrl(WebletConfig config, WebletRequest request, WebletResponse response, URL url, CopyStrategy copyProvider) throws IOException;

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
     */
    public void loadResource(WebletConfig config, WebletRequest request, WebletResponse response, ResourceResolver resourceResolver, CopyStrategy copyStrategy) throws IOException;

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
                                       long resourceLastmodified) throws IOException;

    /**
     * loads a given resource from an input stream it uses internal timestamps for resource handling and resource serving this works on most browser but safari
     * seems to ignore the timestamps and always sends a modifiedSince for resources for 1.1.1970
     *
     * @param config       the weblets config for this resource loading request
     * @param request      the weblets request
     * @param response     the weblets response
     * @param copyProvider a given processing copy provider
     * @param in           the input stream for the processing
     * @throws IOException in case of an internal processing error
     */
    public void loadResourceFromStream(WebletConfig config, WebletRequest request, WebletResponse response, CopyStrategy copyProvider, InputStream in)
            throws IOException;

    /**
     * helper to map an incoming resource path into a resource url
     * used by some resolvers
     * note this method might be deprecated in the future
     *
     * @param resourcePath
     * @return
     */
    public URL getResourceUrl(String resourcePath);

    /**
     * helper to map an incoming resource path into a resource url
     * used by some resolvers
     * note this method might be deprecated in the future
     *
     * @param request
     * @param resourcePath
     * @return
     */
    public URL getResourceUrl(WebletRequest request, String resourcePath);

    /**
     * Returns the resource factory implementation
     * upon the given weblet config
     *
     * @param config
     * @return
     */
    public ResourceFactory getResourceFactory(WebletConfig config);

    /**
     * @param webletName
     * @param webletPath
     * @param contextPath
     * @param webletPathInfo
     * @param ifModifiedSince
     * @param httpRequest
     * @return
     */
    public WebletRequest createWebletRequest(String webletName, String webletPath, String contextPath, String webletPathInfo,long ifModifiedSince, Object httpRequest);

    /**
     * Fetches a processed input stream from a given resource
     *
     * @param config
     * @param request
     * @param resourceResolver
     * @param copyStrategy
     * @return
     * @throws IOException
     */
    public InputStream getResourceInputStream(WebletConfig config, WebletRequest request,  ResourceResolver resourceResolver, CopyStrategy copyStrategy) throws IOException;
}
