/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.weblets.impl.misc;

import net.java.dev.weblets.*;
import net.java.dev.weblets.impl.WebletContainerImpl;
import net.java.dev.weblets.impl.servlets.WebletRequestImpl;
import net.java.dev.weblets.util.IWebletUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.InputStream;

/**
 * A small el function to ease
 * the use of weblets in misc
 * or jsf/faclets jsf 1.2 contexts
 *
 * @author Werner Punz
 */
public class WebletUtilsImpl implements IWebletUtils {

    private static final String WEBLETS_NOT_INITIALIZED = "weblets not initialized, please check the logs";


    public WebletUtilsImpl() {
    }

    /**
     * resolves into a uri relative
     * to the current application context
     * //pathInfo resolves into server absolue addresses
     * <p/>
     * /pathInfo into context relative addresses
     *
     * @param weblet
     * @param pathInfo
     * @return the resource path
     */
    public String getResource(String weblet, String pathInfo) {
        /*lets be more tolerant regarding paths*/
        if (pathInfo == null || pathInfo.trim().equals(""))
            pathInfo = "/";
        WebletContainerImpl container = (WebletContainerImpl) WebletContainer.getInstance();
        if (container == null) {
            initError();

            return WEBLETS_NOT_INITIALIZED;
        }
        return container.getResourceUri(weblet, pathInfo);
    }

    private void initError() {
        Log log = LogFactory.getLog(getClass());
        log.error("The weblet container is null");
        log.error("This is an indication that weblets is not initialized");
        log.error("You might have to add   <listener><listener-class>net.java.dev.weblets.WebletsContextListener</listener-class></listener> to your web.xml!!!!");
    }

    /**
     * tries a backparsing of a given request String into
     * a given backpath
     * note, use this api call with care, it might fail
     * on nested canonical mappings if two weblets have
     * canonical mappings in common the wrong one might trigger
     *
     * @param request    external request if given
     * @param accessPath the access path
     * @return a valid WebletRequest object or null if the backparsing fails
     */
    public WebletRequest getRequestFromPath(Object request, String accessPath) {
        WebletContainerImpl container = (WebletContainerImpl) WebletContainer.getInstance();
        String contextPath = container.getWebletContextPath();
        Map weblets = container.getRegisteredWeblets();


        if (StringUtils.isBlank(contextPath))
            contextPath = "/";

        accessPath = accessPath.trim();

        if (!accessPath.matches(URLREGEXP) && !accessPath.startsWith(contextPath))
            accessPath = contextPath + accessPath;
        if(accessPath.matches(URLREGEXP) && accessPath.indexOf(contextPath) == -1)
            return null;

        String[] parsed =
                container.parseWebletRequest(contextPath, accessPath, -1l);

        //parsing failed
        if (parsed == null || parsed.length == 0)
            return null;

        String webletName = parsed[0];
        String webletPath = parsed[1];

        //we preinit the weblet in case it is not there
        Weblet weblet = container.getWeblet(webletName);

        if (weblet == null) return null;

        String webletPathInfo = parsed[2];
        WebletRequest webRequest =
                new WebletRequestImpl(webletName, webletPath, contextPath, webletPathInfo,
                        -1l, request);

        return webRequest;


    }

    public InputStream getResourceAsStream(WebletRequest request, String mimetype) throws WebletException {
        WebletContainerImpl container = (WebletContainerImpl) WebletContainer.getInstance();
        return container.getResourceStream(request, mimetype);
    }


    public String getURL(String weblet, String pathInfo) {
        /*lets be more tolerant regarding paths*/
        if (pathInfo == null || pathInfo.trim().equals(""))
            pathInfo = "/";
        WebletContainerImpl container = (WebletContainerImpl) WebletContainer.getInstance();
        if (container == null) {
            initError();
            return WEBLETS_NOT_INITIALIZED;
        }
        return container.getWebletContextPath() + container.getResourceUri(weblet, pathInfo);
    }


    static final String URLREGEXP = "^[a-zA-Z]+:.*$";

    //static final String URLREGEXP = "^[a-zA-Z]\\:\\/+[^\\/]+(.*)$";
    //static final Pattern URLPATTERN = Pattern.compile(URLREGEXP);


}
