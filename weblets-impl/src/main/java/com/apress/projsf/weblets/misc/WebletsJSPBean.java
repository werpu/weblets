/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apress.projsf.weblets.misc;

import com.apress.projsf.weblets.WebletContainerImpl;
import net.java.dev.weblets.WebletContainer;

/**
 * A small el function to ease
 * the use of weblets in jsp
 * or jsf/faclets jsf 1.2 contexts
 * 
 * @author Werner Punz
 */
public class WebletsJSPBean {

    public WebletsJSPBean() {
    }

    /**
     * resolves into a uri relative
     * to the current application context
     * //pathInfo resolves into server absolue addresses
     * 
     * /pathInfo into context relative addresses
     * 
     * @param weblet
     * @param path
     * @return
     */
    public static String getResource(String weblet, String pathInfo) {
        WebletContainerImpl container = (WebletContainerImpl) WebletContainer.getInstance();
        return   container.getWebletURL(weblet, pathInfo);
    }
    
    
  
}
