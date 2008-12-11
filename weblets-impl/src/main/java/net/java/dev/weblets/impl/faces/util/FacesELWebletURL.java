package net.java.dev.weblets.impl.faces.util;

import net.java.dev.weblets.impl.faces.util.JSFDummyMap;
import net.java.dev.weblets.FacesWebletUtils;

import javax.faces.context.FacesContext;

/**
 * Legacy JSF EL Helper
 * to resolve jsf urls
 */
public class FacesELWebletURL extends JSFDummyMap {
     private class PathInfoMap extends JSFDummyMap {

        private String webletName = "";

        public PathInfoMap(String webletName) {
          this.webletName = webletName;
        }


        public Object get(Object resource) {
            if(! (resource instanceof String))
                throw new UnsupportedOperationException("Not supported yet.");
            return  FacesWebletUtils.getURL(FacesContext.getCurrentInstance(), webletName ,(String)resource);
        }

    }

    public Object get(Object webletName) {
       if(!(webletName instanceof String))
            throw new UnsupportedOperationException("only String keys are allowed");
       return (new PathInfoMap((String)webletName));
    }
}
