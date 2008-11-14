package net.java.dev.weblets.demo.sourcecodeweblet;

import net.java.dev.weblets.Weblet;
import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.impl.WebletRequestBase;
import net.java.dev.weblets.packaged.ResourceloadingUtils;
import net.java.dev.weblets.util.CopyStrategy;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * a weblet for serving the sourcecode 
 * 
 * @author Werner Punz
 *
 */
public class SourcecodeWeblet extends Weblet {
	  
	    public void service(
	            WebletRequest request,
	            WebletResponse response) throws IOException {
	    	
	    	
	    	WebletRequestBase webletRequest = (WebletRequestBase) request;
	    	
	    	//this might fail on some containers overriding the HttpServlet
	    	//but for demo purposes this is ok
	    	HttpServletRequest httpRequest = (HttpServletRequest) webletRequest.getExternalRequest();
	    	
	    	StringBuffer fullAddr = new StringBuffer(255);
	    	
	    	fullAddr.append(httpRequest.getSession().getServletContext().getRealPath(request.getPathInfo()));
	    	String resourcePath = fullAddr.toString();
	    
	    	
	    	CopyStrategy copyProvider = new SourcecodeCopyStrategy();
	       
	        response.setContentType("text/html");
	        FileInputStream fin = new FileInputStream(resourcePath);

	        ResourceloadingUtils.getInstance().loadResourceFromStream(getWebletConfig(), request, response,  copyProvider, fin);
	    }

    /**
     * Handle reporting cases of containers which are initialized
     * but do not have an active request
     *
     * @param pathInfo The pathinfo to the resource
     * @param mimetype The mimetype
     * @return
     * @throws IOException
     * @throws WebletException
     */
    public InputStream serviceStream(String pathInfo, String mimetype) throws IOException, WebletException {
        return null;
    }

    public void destroy() {
	    super.destroy();
    }
}
