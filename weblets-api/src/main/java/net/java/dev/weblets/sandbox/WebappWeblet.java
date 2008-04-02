package net.java.dev.weblets.sandbox;

import net.java.dev.weblets.*;
import net.java.dev.weblets.packaged.WebletResourceloadingUtils;
import net.java.dev.weblets.util.CopyProviderImpl;
import net.java.dev.weblets.util.CopyProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Weblet for serving resources from a classical
 * webapp structures
 * This is currently only working in a portlet environment
 * if weblets is triggered outside of the portlet context
 * so use it with care!
 * 
 * 
 * @author Werner Punz 
 *
 */
public class WebappWeblet extends Weblet {


    	/**
	 * init method which is called by default
	 * to process the parameters
	 * @param config the webletconfig to be processed
	 */
	public void init(WebletConfig config) {
		super.init(config);
		//fetch the weblets init param
		String packageName = config.getInitParameter("package");
		//fetch the resource root param
		String resourceRoot = config.getInitParameter("resourceRoot");

		//init param missing, lets throw an error
		if (packageName == null && resourceRoot == null) {
			throw new WebletException(
					"Missing either init parameter \"package\" or "
							+ " or init parameter \"resourceRoot\" for "
							+ " Weblet \"" + config.getWebletName() + "\"");
		}
		//the init was successful we now have all we need
		_resourceRoot = (packageName != null) ? packageName.replace('.', '/')
				: resourceRoot;

	}

    public void service(
            WebletRequest request,
            WebletResponse response) throws IOException {

        String resourcePath = _resourceRoot + request.getPathInfo();
        

        //WebletRequestImpl webletRequest = (WebletRequestImpl) request;
    	
    	//this might fail on some containers overriding the HttpServlet
    	//but for demo purposes this is ok
    	HttpServletRequest httpRequest = (HttpServletRequest) request.getExternalRequest();
    	
    	

    	//note, please do not use the filter api for your own filters
    	//yet it will be substantially reworked in 1.1 after that
    	//it will be fully documented and opened for third party
    	//extensions, the current piping api is to complicated

        CopyProvider copyProvider = new CopyProviderImpl();


      	URL url = httpRequest.getSession().getServletContext().getResource(resourcePath);

        WebletResourceloadingUtils.getInstance().loadFromUrl(getWebletConfig(),
				request, response, url, copyProvider);
    }

    public InputStream serviceStream(WebletRequest request, String mimetype) throws IOException, WebletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void destroy() {
		_resourceRoot = null;
		super.destroy();
	}

	private String _resourceRoot;
}

