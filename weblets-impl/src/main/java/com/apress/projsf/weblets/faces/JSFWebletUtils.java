package com.apress.projsf.weblets.faces;


import com.apress.projsf.weblets.misc.WebletsResource;

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
public class JSFWebletUtils {
       
        /**
         * 
         * @param context, the standard faces context
         * @param weblet the weblet name
         * @param pathInfo the path to the resource
         * @return the resource path
         */
        public static String getResource(FacesContext context, String weblet, String pathInfo) {
            return WebletsResource.getResource( weblet, pathInfo);
        }
        
        
        /**
         * 
         * @param context, the standard faces context
         * @param weblet the weblet name
         * @param pathInfo the path the the resources
         * @param UIComponent component, the component for the include
         * @return the resource the resource path, currently the FacesContent
         * and UIComponent are not used but they might be in the future
         */
        public static String getResource(FacesContext context, String weblet, String pathInfo, UIComponent component) {
            return WebletsResource.getResource(weblet, pathInfo);
        }
}