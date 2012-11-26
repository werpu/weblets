package net.java.dev.weblets.impl.weblets;

import net.java.dev.weblets.*;
import net.java.dev.weblets.packaged.ResourceloadingUtils;
import net.java.dev.weblets.util.CopyStrategy;
import net.java.dev.weblets.util.CopyStrategyImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author werpu
 * @date: 18.11.2008
 */
public class URLWebletImpl extends Weblet {
    public int getWebletType() {
        return WebletConfig.WEBLET_TYPE_PROXY;
    }

    public void init(WebletConfig config) {
        super.init(config);
        String httpAddress = config.getInitParameter("rootaddress");
        if (httpAddress == null) {
            throw new WebletException("Missing either init parameter \"package\" or " + " or init parameter \"resourceRoot\" for " + " Weblet \""
                                      + config.getWebletName() + "\"");
        }
        _resourceRoot = httpAddress;
    }

    public void service(WebletRequest request, WebletResponse response) throws IOException {
        String resourcePath = _resourceRoot + request.getPathInfo();
        CopyStrategy copyProvider = new CopyStrategyImpl();
        URL url = new URL(resourcePath);
        ResourceloadingUtils.getInstance().loadFromUrl(getWebletConfig(), request, response, url, copyProvider);
    }

    public InputStream serviceStream(WebletRequest request) throws IOException {
       //TODO implement this
       throw new RuntimeException("Not implemented yet");
    }


    @Override
    public URL getResourceURL(WebletRequest request) throws IOException
    {
        String resourcePath = _resourceRoot + request.getPathInfo();
        return new URL(resourcePath);
    }

    public InputStream serviceStream(String webletname, String mimetype) throws IOException, WebletException {
        return null; // To change body of implemented methods use File | Settings | File Templates.
    }

    public void destroy() {
        _resourceRoot = null;
        super.destroy();
    }

    private String _resourceRoot;
}
