package net.java.dev.weblets;

import net.java.dev.weblets.util.IFacesWebletUtils;

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

    static FacesWebletUtils utils = (FacesWebletUtils) FacesWebletUtils.getInstance();

    public static String getResource(String weblet, String pathInfo) {
        return utils.getResource(FacesContext.getCurrentInstance(), weblet, pathInfo);
    }

    public static String getURL(String weblet, String pathInfo) {
        return utils.getURL(FacesContext.getCurrentInstance(), weblet, pathInfo);
    }

    public static String getResource(String weblet, String pathInfo, boolean suppressDuplicates) {
        return utils.getResource(FacesContext.getCurrentInstance(), weblet, pathInfo, suppressDuplicates);
    }

    public static String getURL(String weblet, String pathInfo, boolean suppressDuplicates) {
        return utils.getURL(FacesContext.getCurrentInstance(), weblet, pathInfo, suppressDuplicates);
    }
}
