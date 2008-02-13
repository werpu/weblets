package net.java.dev.weblets;

import net.java.dev.weblets.util.ServiceLoader;

import java.util.Map;

/**
 * TODO: DESC
 *
 * @author: Werner Punz
 * @date: 13.02.2008.
 */
public  class FacesElWeblet {
    private FacesElWeblet _delegate = null;

    public FacesElWeblet() {
        super();
    }

    private void init() {
         synchronized (FacesElWeblet.class) {
            if (_delegate == null) {
                Class instantiation = ServiceLoader.loadService(FacesElWeblet.class.getName());
                try {
                    _delegate = (FacesElWeblet) instantiation.newInstance();
                } catch (InstantiationException e) {
                    throw new WebletException("Error instantiating FacesElWeblet", e);
                } catch (IllegalAccessException e) {
                    throw new WebletException("Error instantiating FacesElWeblet", e);
                }
            }
        }
    }

     public  Map getResource() {
        if(_delegate == null)
            init();
        return _delegate.getResource();
     }

     /**
     * enables a weblet.url[][] notation on the jsf side of things
     * @return
     */
    public  Map getUrl() {
        if(_delegate == null)
            init();
        return _delegate.getUrl();
    }

}
