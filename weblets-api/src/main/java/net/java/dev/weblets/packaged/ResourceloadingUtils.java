package net.java.dev.weblets.packaged;

import java.io.*;
import java.net.URL;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.resource.*;
import net.java.dev.weblets.util.*;
import net.java.dev.weblets.util.ServiceLoader;

/**
 * helper class to be shared by various weblet loaders
 * This class is an externalized api providing all the needed methods
 * to access resources in a location neutral context
 * and enabling all the functionality
 * for weblets to work!
 *
 * @author Werner Punz
 */
public class ResourceloadingUtils implements IResourceloadingUtils {

    IResourceloadingUtils _delegate = null;
    static IResourceloadingUtils _instance = null;

    ResourceloadingUtils(IResourceloadingUtils delegate) {
        _delegate = delegate;
    }

    public void loadFromUrl(WebletConfig config, WebletRequest request, WebletResponse response, URL url, CopyStrategy copyProvider) throws IOException {
        _delegate.loadFromUrl(config, request, response, url, copyProvider);
    }

    public void loadResource(WebletConfig config, WebletRequest request, WebletResponse response, ResourceResolver resourceResolver, CopyStrategy copyStrategy) throws IOException {
        _delegate.loadResource(config, request, response, resourceResolver, copyStrategy);
    }

    public void loadResourceFromStream(WebletConfig config, WebletRequest request, WebletResponse response, CopyStrategy copyProvider, InputStream in, long resourceLastmodified) throws IOException {
        _delegate.loadResourceFromStream(config, request, response, copyProvider, in, resourceLastmodified);
    }

    public void loadResourceFromStream(WebletConfig config, WebletRequest request, WebletResponse response, CopyStrategy copyProvider, InputStream in) throws IOException {
        _delegate.loadResourceFromStream(config, request, response, copyProvider, in);
    }

    public URL getResourceUrl(String resourcePath) {
        return _delegate.getResourceUrl(resourcePath);
    }

    public URL getResourceUrl(WebletRequest request, String resourcePath) {
        return _delegate.getResourceUrl(request, resourcePath);
    }

    public ResourceFactory getResourceFactory(WebletConfig config) {
        return _delegate.getResourceFactory(config);
    }

    public static IResourceloadingUtils getInstance() {
        if (_instance == null) {
            synchronized (ResourceloadingUtils.class) {
                if (_instance != null) {
                    return _instance;
                }
                Class resourceClass = ServiceLoader.loadService(ResourceloadingUtils.class);
                try {
                    _instance = new ResourceloadingUtils((IResourceloadingUtils) resourceClass.newInstance());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to access " + "WebletsContextListener implementation", e);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Unable to instantiate " + "WebletsContextListener implementation", e);
                }
            }
        }
        return _instance;
    }
}
