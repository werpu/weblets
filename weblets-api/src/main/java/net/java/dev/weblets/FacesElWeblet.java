package net.java.dev.weblets;

import net.java.dev.weblets.util.ServiceLoader;

import java.util.Map;

/**
 * El Weblet utils class api class for the faces el connectors
 * 
 * @author: Werner Punz
 */
public class FacesElWeblet {
	private FacesElWeblet	_delegate	= null;

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

	public Map getResource() {
		if (_delegate == null)
			init();
		return _delegate.getResource();
	}

	/**
	 * enables a weblet.url[][] notation on the jsf side of things
	 * 
	 * @return
	 */
	public Map getUrl() {
		if (_delegate == null)
			init();
		return _delegate.getUrl();
	}
}
