package net.java.dev.weblets.packaged;

import java.io.IOException;
import java.net.URL;

import net.java.dev.weblets.Weblet;
import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.util.IStreamingFilter;
import net.java.dev.weblets.util.WebletTextprocessingFilter;
import net.java.dev.weblets.util.WebletsSimpleBinaryfilter;

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

        IStreamingFilter filterChain = null;
        filterChain = new WebletsSimpleBinaryfilter();
        filterChain.addFilter(new WebletTextprocessingFilter());
        URL url = new URL(resourcePath);

        WebletResourceloadingUtils.getInstance().loadFromUrl(getWebletConfig(), request, response, url, filterChain);
    }

    public void destroy() {
        _resourceRoot = null;
        super.destroy();
    }

    private String _resourceRoot;
}
