package net.java.dev.weblets.packaged;

import net.java.dev.weblets.*;
import net.java.dev.weblets.util.CopyProviderImpl;
import net.java.dev.weblets.util.CopyProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 
 * weblet which streams resources from another 
 * valid url vfs location
 * 
 * it sort of acts as 
 * proxy for remote url resources
 * and can hide the origin of the original weblets
 *
 * note! this stream is under construction
 * and experimental only,
 * it should not be used for production
 * 
 */
public class URLWeblet extends Weblet {

    public int getWebletType() {
        return WebletConfig.WEBLET_TYPE_PROXY;
    }


    public void init(
            WebletConfig config) {
        super.init(config);
        String httpAddress = config.getInitParameter("rootaddress");

        if (httpAddress == null) {
            throw new WebletException("Missing either init parameter \"package\" or " +
                    " or init parameter \"resourceRoot\" for " +
                    " Weblet \"" + config.getWebletName() + "\"");
        }

        _resourceRoot = httpAddress;
    }

    public void service(
            WebletRequest request,
            WebletResponse response) throws IOException {
    	
    	String resourcePath = _resourceRoot + request.getPathInfo();
        CopyProvider copyProvider = new CopyProviderImpl();

        URL url = new URL(resourcePath);

        WebletResourceloadingUtils.getInstance().loadFromUrl(getWebletConfig(), request, response, url, copyProvider);
    }

    public InputStream serviceStream(WebletRequest request, String mimetype) throws IOException, WebletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void destroy() {
        _resourceRoot = null;
        super.destroy();
    }

    private String _resourceRoot;
}
