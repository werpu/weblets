package net.java.dev.weblets.impl.weblets;

import net.java.dev.weblets.*;
import net.java.dev.weblets.packaged.ResourceloadingUtils;
import net.java.dev.weblets.util.CopyStrategyImpl;
import net.java.dev.weblets.resource.ResourceResolver;
import net.java.dev.weblets.resource.ClasspathResourceResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author werpu
 * @date: 18.11.2008
 */
public class PackagedWebletImpl extends Weblet {
    ResourceResolver _resolver = null;

    /**
     * init method which is called by default to process the parameters
     *
     * @param config the webletconfig to be processed
     */
    public void init(WebletConfig config) {
        super.init(config);
        // fetch the weblets init param
        String packageName = config.getInitParameter("package");
        // fetch the resource root param
        String resourceRoot = config.getInitParameter("resourceRoot");
        // String cacheTimeout = config.getInitParameter("cachecontrol-timeout");
        // init param missing, lets throw an error
        if (packageName == null && resourceRoot == null) {
            throw new WebletException("Missing either init parameter \"package\" or " + " or init parameter \"resourceRoot\" for " + " Weblet \""
                                      + config.getWebletName() + "\"");
        }
        // the init was successful we now have all we need
        _resourceRoot = (packageName != null) ? packageName.replace('.', '/') : resourceRoot;
        _resolver = new ClasspathResourceResolver(config, _resourceRoot);
        // optional init param
    }

    public void service(WebletRequest request, WebletResponse response) throws IOException {
        // lets build up our filter chain which in our case is a binary filter for standard
        // processing and our text processing filter for text resources with included
        // weblet: functions
        CopyStrategyImpl copyProvider = new CopyStrategyImpl();
        ResourceloadingUtils.getInstance().loadResource(getWebletConfig(), request, response, _resolver, copyProvider);
    }

    public URL getResourceURL(WebletRequest request) throws IOException {
        String resourcePath = _resourceRoot + request.getPathInfo();

        return _resolver.getURL(request.getPathInfo());
    }

    /**
     * Second Weblet entry point the service stream method is used internally for Weblets 1.1 by our asynchronous reporting interface <p/> It basically does the
     * same as service but must be servlet independend (aka it cannot rely on a base servlet or the external request of the weblet request object <p/> If you do
     * not trigger the reporting subengine then you can omit this interface it is not used internally
     *
     * @param pathInfo
     * @param mimetype
     * @return
     * @throws IOException
     * @throws WebletException
     */
    public InputStream serviceStream(String pathInfo, String mimetype) throws IOException, WebletException {
        String resourcePath = _resourceRoot + pathInfo;
        // lets build up our filter chain which in our case is a binary filter for standard
        // processing and our text processing filter for text resources with included
        // weblet: functions
        //TODO add the new resolver API here as well!
        CopyStrategyImpl copyProvider = new CopyStrategyImpl();
        URL url = _resolver.getURL(pathInfo);
        if (url == null) //TODO add shadow url mock data here
            return null;
        // our utils should handle the standard case
        URLConnection conn = url.openConnection();
        long lastmodified = conn.getLastModified();
        if (mimetype == null)
            mimetype = getWebletConfig().getMimeType(resourcePath);
        return copyProvider.wrapInputStream(getWebletConfig().getWebletName(), mimetype, conn.getInputStream());
    }

    public void destroy() {
        _resourceRoot = null;
        super.destroy();
    }

    private String _resourceRoot;
}
