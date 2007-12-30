package net.java.dev.weblets;

import net.java.dev.weblets.util.ServiceLoader;
import net.java.dev.weblets.util.IJSFWebletsUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Werner Punz
 *         Weblet Utils class which gives the interfaces
 *         for the JSF part of weblets
 *         <p/>
 *         Date: 30.12.2007
 *         Time: 13:44:04
 */
public class JSFWebletsUtils {

    public static String getResource(FacesContext context, String weblet, String pathInfo) {
        return instance.getResource(context, weblet, pathInfo);
    }


    public static String getResource(FacesContext context, String weblet, String pathInfo, UIComponent component) {
        return instance.getResource(context, weblet, pathInfo, component);
    }


    /**
    * kind of a weird construct but definitely faster
    * than doing all the calls over introspection, the internal
    * contract is defined by the IJSFWebletsUtils interface
    */
    static IJSFWebletsUtils instance = getInstance();

    static IJSFWebletsUtils getInstance() throws WebletException {
        synchronized (JSFWebletsUtils.class) {
            if (instance == null) {
                Class instantiation = ServiceLoader.loadService(JSFWebletsUtils.class.getName());
                try {
                    instance = (IJSFWebletsUtils) instantiation.newInstance();
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
