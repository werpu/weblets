package net.java.dev.weblets;

import net.java.dev.weblets.util.ServiceLoader;
import net.java.dev.weblets.util.IWebletsUtil;

/**
 * @author Werner Punz
 * Date: 30.12.2007
 * Time: 18:00:04
 */
public class WebletsUtils {

    /**
     * constractual method
     *
     * @param weblet  the weblet name
     * @param pathInfo  the path info to the resource
     * @return the resource path for the resource
     */
     public static String getResource(String weblet, String pathInfo) {
         return instance.getResource(weblet, pathInfo);
     }

    /**
     * constractual method
     *
     * @param weblet  the weblet name
     * @param pathInfo  the path info to the resource
     * @return the resource path for the resource
     */
     public static String getUrl(String weblet, String pathInfo) {
         return instance.getUrl(weblet, pathInfo);
     }


        /**
    * kind of a weird construct but definitely faster
    * than doing all the calls over introspection, the internal
    * contract is defined by the IJSFWebletsUtils interface
    */
    static IWebletsUtil instance = getInstance();

    static IWebletsUtil getInstance() throws WebletException {
        synchronized (FacesWebletUtils.class) {
            if (instance == null) {
                Class instantiation = ServiceLoader.loadService(WebletsUtils.class.getName());
                try {
                    instance = (IWebletsUtil) instantiation.newInstance();
                } catch (InstantiationException e) {
                    throw new WebletException("Error instantiating WebletsUtil", e);
                } catch (IllegalAccessException e) {
                    throw new WebletException("Error instantiating WebletsUtil", e);
                }
            }
        }
        return instance;
    }
}
