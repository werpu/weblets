package net.java.dev.weblets.impl.faces;

import net.java.dev.weblets.WebletUtils;
import net.java.dev.weblets.util.IFacesWebletUtils;

import javax.faces.context.FacesContext;

/**
 * a faces util class which allows the easy inclusion of weblets for component authors this one should replace the weblets view handler in the long run
 * 
 * @author Werner Punz
 * @deprecated
 */
public class FacesWebletUtilsImpl implements IFacesWebletUtils {
	/**
	 * 
	 * @param context
	 *            , the standard faces context
	 * @param weblet
	 *            the weblet name
	 * @param pathInfo
	 *            the path to the resource
	 * @return the resource path
	 */
	public String getResource(FacesContext context, String weblet, String pathInfo, boolean url) {
		if (url)
			return context.getApplication().getViewHandler().getResourceURL(context, WebletUtils.getResource(weblet, pathInfo));
		return WebletUtils.getResource(weblet, pathInfo);
	}
}
