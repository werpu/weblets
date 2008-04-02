package net.java.dev.weblets.sandbox;

import net.java.dev.weblets.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * Weblet for having redirection mechanisms
 * to have resources externally
 * and do not trigger the weblet resource loading 
 * at all!
 * 
 * @author Werner Punz 
 *
 */
public class RedirectionWeblet extends Weblet {

    /**
     * special case of redirection weblets
     * which are our subdomain here
     * 
     * @return
     */
    public int getWebletType() {
        return WebletConfig.WEBLET_TYPE_REDIRECT;
    }

    String _resourceRoot = "";

           /**
	 * init method which is called by default
	 * to process the parameters
	 * @param config the webletconfig to be processed
	 */
	public void init(WebletConfig config) {
		super.init(config);


        _resourceRoot = config.getInitParameter("resourceRoot");

		//init param missing, lets throw an error
		if (_resourceRoot == null) {
			throw new WebletException(
					"Missing either init parameter \"package\" or "
							+ " or init parameter \"resourceRoot\" for "
							+ " Weblet \"" + config.getWebletName() + "\"");
		}
		
	}

    public void service(WebletRequest request, WebletResponse response) throws IOException, WebletException {
        //we do nothing here since our indirection
        //api will take care of it
    }

    public InputStream serviceStream(WebletRequest request, String mimetype) throws IOException, WebletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
