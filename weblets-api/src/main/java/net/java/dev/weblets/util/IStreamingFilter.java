package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Generic Streaming Filter interface
 * This is a generic streaming filter
 * which can be utilized
 * for various stream processing
 * filters, ie, currently only
 * by the text parsing filter which
 * processes the text files for weblets
 * but in the long run
 * other filters can be applied
 * like compression filters
 * or watermarking filters
 * 
 *
 * @author Werner Punz
 */
public interface IStreamingFilter {
    public void addFilter(IStreamingFilter filter); 
    public void filter(WebletConfig config, WebletRequest request,  WebletResponse response, InputStream in, OutputStream out) throws IOException;
}
