package net.java.dev.weblets;

import net.java.dev.weblets.util.IFacesWebletUtils;
import net.java.dev.weblets.util.ServiceLoader;

import javax.faces.context.FacesContext;

/**
 * @author Werner Punz Weblet Utils class which gives the interfaces for the JSF part of weblets
 */
public class FacesWebletUtils {
	/**
	 * returns the absolute url with the context path
	 * 
	 * @param context
	 *            the current faces context
	 * @param weblet
	 *            the weblet name
	 * @param pathInfo
	 *            the path info
	 * @return a url with the current web-app context path to the weblet
	 */
	public static String getURL(FacesContext context, String weblet, String pathInfo) {
		return instance.getResource(context, weblet, pathInfo, true);
	}

	/**
	 * returns the relative url without the context path
	 * 
	 * @param context
	 *            the current faces context
	 * @param weblet
	 *            the weblet name
	 * @param pathInfo
	 *            the path info to the resource
	 * @return a url with the current web-app context path to the weblet
	 */
	public static String getResource(FacesContext context, String weblet, String pathInfo) {
		return instance.getResource(context, weblet, pathInfo, false);
	}

	/**
	 * kind of a weird construct but definitely faster than doing all the calls over introspection, the internal contract is defined by the IFacesWebletUtils
	 * interface
	 */
	static IFacesWebletUtils	instance	= getInstance();

	static IFacesWebletUtils getInstance() throws WebletException {
		synchronized (FacesWebletUtils.class) {
			if (instance == null) {
				Class instantiation = ServiceLoader.loadService(FacesWebletUtils.class.getName());
				try {
					instance = (IFacesWebletUtils) instantiation.newInstance();
				} catch (InstantiationException e) {
					throw new WebletException("Error instantiating IJSFWebletsUtils", e);
				} catch (IllegalAccessException e) {
					throw new WebletException("Error instantiating IJSFWebletsUtils", e);
				}
			}
		}
		return instance;
	}
}
