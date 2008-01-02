package net.java.dev.weblets.impl.faces;

import net.java.dev.weblets.WebletsUtils;
import net.java.dev.weblets.util.IJSFWebletsUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * a faces utils class
 * which allows the easy inclusion of weblets for
 * component authors
 * this one should replace the weblets view handler
 * in the long run
 * 
 * @author Werner Punz
 *
 */
public class JSFWebletsUtilsImpl implements IJSFWebletsUtils {
        
        /**
         * 
         * @param context, the standard faces context
         * @param weblet the weblet name
         * @param pathInfo the path to the resource
         * @return the resource path
         */
        public String getResource(FacesContext context, String weblet, String pathInfo, boolean url) {
            if(url)
               return context.getApplication().getViewHandler().getResourceURL(context, WebletsUtils.getResource(weblet, pathInfo));
            return WebletsUtils.getResource(weblet, pathInfo);
        }
       
}
