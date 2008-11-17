package net.java.dev.weblets;

import net.java.dev.weblets.util.IFacesWebletUtils;
import net.java.dev.weblets.util.AttributeUtils;

import javax.faces.context.FacesContext;
import java.io.InputStream;

/**
 * @author werpu
 * @date: 13.11.2008
 * <p/>
 * Specialized implementation
 * of the weblet utils
 * for easier handling within
 * a faces EL weblet context!
 */
public class FaceletELWebletUtils {

    /**
     * returns the relative url without the context path
     *
     * @param weblet   the weblet name
     * @param pathInfo the path info to the resource
     * @return a url with the current web-app context path to the weblet
     */
    public static String getResource(String weblet, String pathInfo) {
        return FacesWebletUtils.getResource(FacesContext.getCurrentInstance(), weblet, pathInfo);
    }

    /**
     * returns the absolute url with the context path
     *
     * @param weblet   the weblet name
     * @param pathInfo the path info
     * @return a url with the current web-app context path to the weblet
     */
    public static String getURL(String weblet, String pathInfo) {
        return FacesWebletUtils.getURL(FacesContext.getCurrentInstance(), weblet, pathInfo);
    }

    /**
     * getResource which also suppresses double includes
     * for internal handling
     * comes in handy with the subbundles which always deliver the bundle id as resource!
     *
     * @param weblet
     * @param pathInfo
     * @param suppressDuplicates
     * @return
     */
    public static String getResource(String weblet, String pathInfo, boolean suppressDuplicates) {
        return FacesWebletUtils.getResource(FacesContext.getCurrentInstance(), weblet, pathInfo, suppressDuplicates);
    }

    /**
     * internal api which can be used by component
     * sets to suppress double includes
     * for jsf components!
     *
     * @param weblet
     * @param pathInfo
     * @param suppressDuplicates
     * @return
     */
    public static String getURL(String weblet, String pathInfo, boolean suppressDuplicates) {
        return FacesWebletUtils.getURL(FacesContext.getCurrentInstance(), weblet, pathInfo, suppressDuplicates);
    }

    /**
     * isResourceLoaded check
     *
     * @param weblet   the weblet
     * @param pathInfo the pathInfo
     * @return
     */
    public static boolean isResourceLoaded(String weblet, String pathInfo) {
        return FacesWebletUtils.isResourceLoaded(FacesContext.getCurrentInstance(), weblet, pathInfo);
    }
}
