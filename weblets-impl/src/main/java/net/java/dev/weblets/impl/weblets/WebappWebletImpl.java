package net.java.dev.weblets.impl.weblets;

import net.java.dev.weblets.Weblet;
import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.packaged.ResourceloadingUtils;
import net.java.dev.weblets.resource.WebappResourceResolver;
import net.java.dev.weblets.util.CopyStrategy;
import net.java.dev.weblets.util.CopyStrategyImpl;
import net.java.dev.weblets.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author werpu
 * @date: 18.11.2008
 * <p/>
 * Implementation class for the webapp weblet!
 */
public class WebappWebletImpl extends Weblet
{
    String _packageName = "";
    private String _resourceRoot;
    private WebappResourceResolver _urlResolver;

    /**
     * init method which is called by default to process the parameters
     *
     * @param config the webletconfig to be processed
     */
    public void init(WebletConfig config)
    {
        super.init(config);
        // fetch the weblets init param
        _packageName = config.getInitParameter("package");
        // fetch the resource root param
        /*auto fix path according to the specs the root must begin with a leading /*/
        _resourceRoot = config.getInitParameter("resourceRoot");
        if (StringUtils.isBlank(_resourceRoot))
            _resourceRoot = "/";
        _resourceRoot = _resourceRoot.trim();
        if (!_resourceRoot.startsWith("/") && !_resourceRoot.startsWith("\\"))
            _resourceRoot = "/" + _resourceRoot;
        // init param missing, lets throw an error
        if (_packageName == null && _resourceRoot == null)
        {
            throw new WebletException("Missing either init parameter \"package\" or " + " or init parameter \"resourceRoot\" for " + " Weblet \""
                    + config.getWebletName() + "\"");
        }
        // the init was successful we now have all we need
        _resourceRoot = (_packageName != null) ? _packageName.replace('.', '/') : _resourceRoot;
        _urlResolver = new WebappResourceResolver(config, _resourceRoot);
    }

    public void service(WebletRequest request, WebletResponse response) throws IOException
    {
        String resourcePath = _resourceRoot + request.getPathInfo();
        // this might fail on some containers overriding the HttpServlet
        // but for demo purposes this is ok
        /*we now have the resource handling decoupled from the mimetypes*/
        assertSecurity(resourcePath); //additional security constraints!
        CopyStrategy copyProvider = new CopyStrategyImpl();
        ResourceloadingUtils.getInstance().loadResource(getWebletConfig(), request, response, _urlResolver, copyProvider);
    }

    @Override
    public URL getResourceURL(WebletRequest request) throws IOException
    {
        String resourcePath = _resourceRoot + request.getPathInfo();
        return new URL(resourcePath);
    }

    @Override
    public InputStream serviceStream(WebletRequest request) throws IOException
    {
        String resourcePath = _resourceRoot + request.getPathInfo();
        assertSecurity(resourcePath); //additional security constraints!
        CopyStrategy copyProvider = new CopyStrategyImpl();
        return ResourceloadingUtils.getInstance().getResourceInputStream(getWebletConfig(), request, _urlResolver, copyProvider);
    }

    private void assertSecurity(String resourcePath)
    {
        if (resourcePath.indexOf("META-INF/") != -1 || resourcePath.indexOf("META-INF\\") != -1 ||
                resourcePath.indexOf("WEB-INF/") != -1 || resourcePath.indexOf("WEB-INF\\") != -1)
        {
            throw new SecurityException("Access to WEB-INF or META-INF is not permitted");
        }
    }

    public InputStream serviceStream(String pathInfo, String mimetype) throws IOException, WebletException
    {
        return null; // To change body of implemented methods use File |
        // Settings | File Templates.
    }

    public void destroy()
    {
        _resourceRoot = null;
        _packageName = null;
        super.destroy();
    }
}
