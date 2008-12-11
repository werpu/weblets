package net.java.dev.weblets.util;

import java.io.InputStream;

/**
 * @author Werner Punz Internal Contract for Generic non jsf resource util classes
 * 
 */
public interface IWebletUtils {
	public String getResource(String weblet, String pathInfo);

	public String getURL(String weblet, String pathInfo);

    /**
     * Reporting case for weblets
     * this methid must be able to deal with requests being null
     * (Special testcases have to be applied to check this!
     *
     * @param weblet    the weblet name
     * @param pathInfo  the pathinfo
     * @param mimeType  the mimetype
     * @return
     */
    public InputStream getResourceStream(String weblet, String pathInfo, String mimeType);
}
