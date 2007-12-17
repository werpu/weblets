/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apress.projsf.weblets.misc;

import com.apress.projsf.weblets.WebletContainerImpl;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
     * helper function for the weblet:// resource url
     * resolution
     * 
     * @param url the weblet:// path
     * @return a url which triggers a weblet load on the resource
     */
    public static String getResourceUrl(String url) {
        Matcher matcher = WEBLET_PROTOCOL.matcher(url);
        matcher.matches();
        String weblet = matcher.group(1);
        String pathInfo = matcher.group(2);

        WebletContainerImpl container = (WebletContainerImpl) WebletContainer.getInstance();
        return container.getWebletURL(weblet, pathInfo);
    }
    private static final Pattern WEBLET_PROTOCOL =
            Pattern.compile("weblet://([^/]+)(/.*)?");
}
