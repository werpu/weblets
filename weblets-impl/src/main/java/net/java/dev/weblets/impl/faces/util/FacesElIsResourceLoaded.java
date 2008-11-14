package net.java.dev.weblets.impl.faces.util;

import net.java.dev.weblets.FacesWebletUtils;

import javax.faces.context.FacesContext;

/**
 * @author werpu
 * @date: 14.11.2008
 * <p/>
 * Dummy map which implements the isResourceLoaded operation
 * <p/>
 * usage webletUtils.resourceLoaded['webletname']['pathInfo']
 * returns a java.lang.Boolean Object which then can be further processed
 * (or is processed by the EL in our case)
 */
public class FacesElIsResourceLoaded extends JSFDummyMap {
    private class PathInfoMap extends JSFDummyMap {

        private String webletName = "";

        public PathInfoMap(String webletName) {
            this.webletName = webletName;
        }

        public Object get(Object resource) {
            if (!(resource instanceof String))
                throw new UnsupportedOperationException("Not supported yet.");
            return new Boolean(FacesWebletUtils.isResourceLoaded(FacesContext.getCurrentInstance(), webletName, (String) resource));
        }
    }

    public Object get(Object webletName) {
        if (!(webletName instanceof String))
            throw new UnsupportedOperationException("only String keys are allowed");
        return (new PathInfoMap((String) webletName));
    }
}
