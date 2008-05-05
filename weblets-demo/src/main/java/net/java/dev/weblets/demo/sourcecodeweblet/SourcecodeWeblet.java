package net.java.dev.weblets.demo.sourcecodeweblet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import net.java.dev.weblets.Weblet;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.util.IWebletUtils;
import net.java.dev.weblets.util.CopyProvider;
import net.java.dev.weblets.impl.WebletRequestBase;
import net.java.dev.weblets.packaged.WebletResourceloadingUtils;

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
	    
	    	
	    	CopyProvider copyProvider = new SourcecodeCopyProvider();
	       
	        response.setContentType("text/html");
	        FileInputStream fin = new FileInputStream(resourcePath);

	        WebletResourceloadingUtils.getInstance().loadResourceFromStream(getWebletConfig(), request, response,  copyProvider, fin);
	    }

    public InputStream serviceStream(String pathInfo, String mimetype) throws IOException, WebletException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void destroy() {
	        super.destroy();
	    }
}
