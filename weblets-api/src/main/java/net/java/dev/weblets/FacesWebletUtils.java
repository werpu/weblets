package net.java.dev.weblets;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

/**
 * @author Werner Punz Weblet Utils class which gives the interfaces for the JSF part of weblets
 */
public class FacesWebletUtils
{

    public static final String SINGLETON_HOLDER = "webletsRequestSingletonHolder";

    /**
     * returns the absolute url with the context path
     *
     * @param context  the current faces context
     * @param weblet   the weblet name
     * @param pathInfo the path info
     * @return a url with the current web-app context path to the weblet
     */
    public static String getURL(FacesContext context, String weblet, String pathInfo)
    {

        return WebletUtils.getURL(weblet, pathInfo);
    }

    private static RequestSingletonHolder getSingletinHolder(FacesContext context)
    {
        RequestSingletonHolder singletonHolder = (RequestSingletonHolder) context.getApplication().getVariableResolver().resolveVariable(context, SINGLETON_HOLDER);
        return singletonHolder;
    }

    /**
     * internal api which can be used by component
     * sets to suppress double includes
     * for jsf components!
     *
     * @param context
     * @param weblet
     * @param pathInfo
     * @param suppressDoubleIncludes
     * @return
     */
    public static String getURL(FacesContext context, String weblet, String pathInfo, boolean suppressDoubleIncludes)
    {
        return WebletUtils.getURL(getSingletinHolder(context), weblet, pathInfo, suppressDoubleIncludes);
    }

    /**
     * getResource which also suppresses double includes
     * for internal handling
     * comes in handy with the subbundles which always deliver the bundle id as resource!
     *
     * @param context
     * @param weblet
     * @param pathInfo
     * @param suppressDoubleIncludes
     * @return
     */
    public static String getResource(FacesContext context, String weblet, String pathInfo, boolean suppressDoubleIncludes)
    {
        return WebletUtils.getResource(getSingletinHolder(context), weblet, pathInfo, suppressDoubleIncludes);
    }

    /**
     * returns the relative url without the context path
     *
     * @param context  the current faces context
     * @param weblet   the weblet name
     * @param pathInfo the path info to the resource
     * @return a url with the current web-app context path to the weblet
     */
    public static String getResource(FacesContext context, String weblet, String pathInfo)
    {
        return WebletUtils.getResource(weblet, pathInfo);
    }

    /**
     * isResourceLoaded check
     *
     * @param context  the current facesContext
     * @param weblet   the weblet
     * @param pathInfo the pathInfo
     * @return
     */
    public static boolean isResourceLoaded(FacesContext context, String weblet, String pathInfo)
    {
        return WebletUtils.isResourceLoaded(getSingletinHolder(context), weblet, pathInfo);
    }

    public static String getJSFResource(FacesContext ctx, String weblet, String pathInfo)
    {
        ResourceHandler handler = ctx.getApplication().getResourceHandler();

        Resource resource = handler.createResource(pathInfo, weblet);
        String requestURL = resource.getRequestPath();
        String contextPath = ctx.getExternalContext().getRequestContextPath();
        if (requestURL.startsWith(contextPath))
        {
            requestURL = requestURL.substring(contextPath.length() + 1);
        }
        return requestURL;
    }

    /**
     * fetches a faces 2.0 compliant resource url
     * which utilizes the jsf weblet resource handler
     * and the jsf 2.0 weblet resource
     *
     * @param weblet
     * @param pathInfo
     * @return the resource url for the resource
     */
    public static String getJSFURL(FacesContext ctx, String weblet, String pathInfo)
    {
        ResourceHandler handler = ctx.getApplication().getResourceHandler();

        Resource resource = handler.createResource(pathInfo, weblet);
        return resource.getRequestPath();
    }

}
