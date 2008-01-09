package net.java.dev.weblets.packaged;

import net.java.dev.weblets.*;
import net.java.dev.weblets.util.IStreamingFilter;
import net.java.dev.weblets.util.WebletsSimpleBinaryfilter;
import net.java.dev.weblets.util.WebletTextprocessingFilter;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * weblet which streams resources from another http connection
 * it sort of acts as proxy for remote http resources
 * and can hide the origin of the original weblets
 *
 * note! this stream is under construction
 * 
 */
public class HttpWeblet extends Weblet {

    public void init(
            WebletConfig config) {
        super.init(config);
        String httpAddress = config.getInitParameter("address");
        String resourceRoot = config.getInitParameter("resourceRoot");

        if (httpAddress == null && resourceRoot == null) {
            throw new WebletException("Missing either init parameter \"package\" or " +
                    " or init parameter \"resourceRoot\" for " +
                    " Weblet \"" + config.getWebletName() + "\"");
        }
        _resourceRoot = (httpAddress != null) ? httpAddress
                : resourceRoot;

    }

    public void service(
            WebletRequest request,
            WebletResponse response) throws IOException {
        String resourcePath = _resourceRoot + request.getPathInfo();

        URL url = new URL(resourcePath);

        IStreamingFilter filterChain = null;
        filterChain = new WebletsSimpleBinaryfilter();
        filterChain.addFilter(new WebletTextprocessingFilter());
        WebletResourceloadingUtils.getInstance().loadFromUrl(getWebletConfig(), request, response, url, filterChain);
    }



    public void destroy() {
        _resourceRoot = null;
        super.destroy();
    }

    private String _resourceRoot;


}
