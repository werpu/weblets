package com.apress.projsf.weblets.faces;

import com.apress.projsf.weblets.misc.WebletsJSPBean;
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
public class WebletFacesUtils {
	

        /**
         * @param context, the standard faces context
         * @param webletUrl a weblet in weblet:// notation
         * @returns the resource for simple cases
         * without any jsf component context
         */
	public String getResourceUrl(FacesContext context, String webletUrl) {
            return WebletsJSPBean.getResourceUrl((String)webletUrl); 
        }
        
        /**
         * 
         * @param context, the standard faces context
         * @param weblet the weblet name
         * @param pathInfo the path to the resource
         * @return the resource path
         */
        public String getResourceUrl(FacesContext context, String weblet, String pathInfo) {
            return WebletsJSPBean.getResourceUrl(weblet, pathInfo);
        }
        
         /**
         * @param context, the standard faces context
         * @param webletUrl a weblet in weblet:// notation
         * @param UIComponent component, the component for the include
         * @return the resource the resource path, currently the FacesContent
         * and UIComponent are not used but they might be in the future
         * 
         */
	public String getResourceUrl(FacesContext context, String webletUrl, UIComponent component) {
            return WebletsJSPBean.getResourceUrl((String)webletUrl); 
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
        public String getResourceUrl(FacesContext context, String weblet, String pathInfo, UIComponent component) {
            return WebletsJSPBean.getResourceUrl(weblet, pathInfo);
        }
}
