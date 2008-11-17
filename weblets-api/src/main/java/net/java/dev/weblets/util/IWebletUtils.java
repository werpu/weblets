package net.java.dev.weblets.util;

import javax.servlet.ServletRequest;
import java.io.InputStream;

/**
 * @author Werner Punz Internal Contract for Generic non jsf resource util classes
 */
public interface IWebletUtils {

    /**
     * returns a resource without a double
     * include check
     *
     * @param weblet   the weblet name
     * @param pathInfo the pathinfo for the resource
     * @return the uri to the resource without the webapp context
     */
    public String getResource(String weblet, String pathInfo);

    /**
     * get the url for a resource with the webapp context
     * and without a double check!
     *
     * @param weblet
     * @param pathInfo
     * @return
     */
    public String getURL(String weblet, String pathInfo);

    /**
     * Get resource with a simple check
     * for double includes within the current request
     *
     * @param requestSingletonHolder a holder with a data structure
     *                               which can block subsequet requests for this resource
     *                               the singlethon holder has to implemenent the getAttribute
     *                               and setAttribute Methods!  A helper interface
     *                               is provided in the Weblet API to ease this contractual
     *                               binding
     * @param weblet                 the weblet name for this resource
     * @param pathInfo               the pathInfo for this resource
     * @param suppressDuplicates     if set to true, duplicate includes result in empty return values
     * @return the includable resource uri without the application context
     * @see net.java.dev.weblets.RequestSingletonHolder for further
     *      reference
     */
    public String getResource(Object requestSingletonHolder, String weblet, String pathInfo, boolean suppressDuplicates);

    /**
     * getURL with a simple check on double includes
     *
     * @param requestSingletonHolder a holder with a data structure
     *                               which can block subsequet requests for this resource
     *                               the singlethon holder has to implemenent the getAttribute
     *                               and setAttribute Methods!  A helper interface
     *                               is provided in the Weblet API to ease this contractual
     *                               binding
     * @param weblet                 the weblet name for this resource
     * @param pathInfo               the pathInfo for this resource
     * @param suppressDuplicates     if set to true, duplicate includes result in empty return values
     * @return the includable resource uri with the application context
     * @see net.java.dev.weblets.RequestSingletonHolder for further
     *      reference
     */
    public String getURL(Object requestSingletonHolder, String weblet, String pathInfo, boolean suppressDuplicates);

    /**
     * check if a resource already is loaded
     *
     * @param requestSingletonHolder
     * @param weblet
     * @param pathInfo
     * @return
     */
    public boolean isResourceLoaded(Object requestSingletonHolder, String weblet, String pathInfo);

    /**
     * Reporting case for weblets
     * this methid must be able to deal with requests being null
     * (Special testcases have to be applied to check this!
     *
     * @param weblet   the weblet name
     * @param pathInfo the pathinfo
     * @param mimeType the mimetype
     * @return
     */
    public InputStream getResourceStream(String weblet, String pathInfo, String mimeType);
}
