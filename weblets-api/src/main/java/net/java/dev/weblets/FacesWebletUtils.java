package net.java.dev.weblets;

import net.java.dev.weblets.util.IFacesWebletUtils;
import net.java.dev.weblets.util.ServiceLoader;
import net.java.dev.weblets.util.AttributeUtils;

import javax.faces.context.FacesContext;

/**
 * @author Werner Punz Weblet Utils class which gives the interfaces for the JSF part of weblets
 */
public class FacesWebletUtils {
    private static final String RES_SERVED = "RES_SERVED";
    private static final String ERR_INSTANTIATION = "Error instantiating IJSFWebletsUtils";

    /**
     * returns the absolute url with the context path
     *
     * @param context  the current faces context
     * @param weblet   the weblet name
     * @param pathInfo the path info
     * @return a url with the current web-app context path to the weblet
     */
    public static String getURL(FacesContext context, String weblet, String pathInfo) {
        return instance.getResource(context, weblet, pathInfo, true);
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
    public static String getURL(FacesContext context, String weblet, String pathInfo, boolean suppressDoubleIncludes) {
        if (suppressDoubleIncludes) {
            String url = instance.getResource(context, weblet, pathInfo, false);
            Object req = context.getExternalContext().getRequest();
            if (AttributeUtils.getAttribute(req, RES_SERVED + url) != null) {
                return "";
            } else {
                AttributeUtils.setAttribute(req, RES_SERVED + url, Boolean.TRUE);
            }
        }
        return instance.getResource(context, weblet, pathInfo, true);
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
    public static String getResource(FacesContext context, String weblet, String pathInfo, boolean suppressDoubleIncludes) {
        String url = instance.getResource(context, weblet, pathInfo, false);
        if (suppressDoubleIncludes) {
            Object req = (Object) context.getExternalContext().getRequest();
            if (AttributeUtils.getAttribute(req, RES_SERVED + url) != null) {
                return "";
            } else {
                AttributeUtils.setAttribute(req, RES_SERVED + url, Boolean.TRUE);
            }
        }
        return url;
    }

    /**
     * returns the relative url without the context path
     *
     * @param context  the current faces context
     * @param weblet   the weblet name
     * @param pathInfo the path info to the resource
     * @return a url with the current web-app context path to the weblet
     */
    public static String getResource(FacesContext context, String weblet, String pathInfo) {
        return instance.getResource(context, weblet, pathInfo, false);
    }

    /**
     * isResourceLoaded check
     *
     * @param context  the current facesContext
     * @param weblet   the weblet
     * @param pathInfo the pathInfo
     * @return
     */
    public static boolean isResourceLoaded(FacesContext context, String weblet, String pathInfo) {
        String url = instance.getResource(context, weblet, pathInfo, false);
        Object req = (Object) context.getExternalContext().getRequest();
        return AttributeUtils.getAttribute(req, RES_SERVED + url) != null;
    }

    /**
     * kind of a weird construct but definitely faster than doing all the calls over introspection, the internal contract is defined by the IFacesWebletUtils
     * interface
     */
    static IFacesWebletUtils instance = getInstance();

    static IFacesWebletUtils getInstance() throws WebletException {
        synchronized (FacesWebletUtils.class) {
            if (instance == null) {
                Class instantiation = ServiceLoader.loadService(FacesWebletUtils.class.getName());
                try {
                    instance = (IFacesWebletUtils) instantiation.newInstance();
                } catch (InstantiationException e) {
                    throw new WebletException(ERR_INSTANTIATION, e);
                } catch (IllegalAccessException e) {
                    throw new WebletException(ERR_INSTANTIATION, e);
                }
            }
        }
        return instance;
    }
}
