/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.dev.weblets.impl.faces.util;

import net.java.dev.weblets.FacesWebletUtils;
import net.java.dev.weblets.impl.faces.util.JSFDummyMap;

import javax.faces.context.FacesContext;

/**
 * Pseudo map for accessing the weblet url format
 * from legacy jsf applications via elwelbetResolver
 * <h:outputFormat value="#{webletUrl['weblet']['/test.js']}" />
 *
 * @author Werner Punz
 */
public class FacesElWebletResource extends JSFDummyMap {

    private class PathInfoMap extends JSFDummyMap {

        private String webletName = "";

        public PathInfoMap(String webletName) {
            this.webletName = webletName;
        }

        public Object get(Object resource) {
            if (!(resource instanceof String))
                throw new UnsupportedOperationException("Not supported yet.");
            return FacesWebletUtils.getResource(FacesContext.getCurrentInstance(), webletName, (String) resource);
        }
    }

    public Object get(Object webletName) {
        if (!(webletName instanceof String))
            throw new UnsupportedOperationException("only String keys are allowed");
        return (new PathInfoMap((String) webletName));
    }
}