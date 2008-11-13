package net.java.dev.weblets.impl.faces.util;

import net.java.dev.weblets.FacesWebletUtils;

import javax.faces.context.FacesContext;

/**
 * @author werpu
 * @date: 13.11.2008
 * <p/>
 * extended resource with duplicate resource check!
 */
public class FacesElExtWebletResource extends JSFDummyMap {

    private class DuplicateCheckMap extends JSFDummyMap {

        private String webletName = "";
        private String resourcePath = "";

        public DuplicateCheckMap(String webletName, String resourcePath) {
            this.webletName = webletName;
            this.resourcePath = resourcePath;
        }

        public Object get(Object resource) {
            if (!(resource instanceof Boolean))
                throw new UnsupportedOperationException("Not supported yet.");
            return FacesWebletUtils.getResource(FacesContext.getCurrentInstance(), webletName, resourcePath, ((Boolean) resource).booleanValue());
        }
    }

    private class PathInfoMap extends JSFDummyMap {

        private String webletName = "";

        public PathInfoMap(String webletName) {
            this.webletName = webletName;
        }

        public Object get(Object resource) {
            if (!(resource instanceof String))
                throw new UnsupportedOperationException("Not supported yet.");
            return new DuplicateCheckMap(webletName, (String) resource);
        }
    }

    public Object get(Object key) {
        if (!(key instanceof String))
            throw new UnsupportedOperationException("Not supported yet.");
        return new PathInfoMap((String) key);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
