package net.java.dev.weblets.demo.sourcecodeweblet;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.java.dev.weblets.Weblet;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.impl.servlets.WebletRequestImpl;
import net.java.dev.weblets.packaged.WebletResourceloadingUtils;
import net.java.dev.weblets.util.IStreamingFilter;

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
	    	
	    	
	    	WebletRequestImpl webletRequest = (WebletRequestImpl) request;
	    	
	    	//this might fail on some containers overriding the HttpServlet
	    	//but for demo purposes this is ok
	    	HttpServletRequest httpRequest = (HttpServletRequest) webletRequest.getHttpRequest();
	    	
	    	StringBuffer fullAddr = new StringBuffer(255);
	    	
	    	fullAddr.append(httpRequest.getSession().getServletContext().getRealPath(request.getPathInfo()));
	    	String resourcePath = fullAddr.toString();
	    
	    	//note, please do not use the filter api for your own filters
	    	//yet it will be substantially reworked in 1.1 after that
	    	//it will be fully documented and opened for third party
	    	//extensions, the current piping api is to complicated
	    	IStreamingFilter filter = null;
	        
	        filter = new WebletsSourcepageFilter();
	       
	        response.setContentType("text/html");
	        FileInputStream fin = new FileInputStream(resourcePath);

	        WebletResourceloadingUtils.getInstance().loadResourceFromStream(getWebletConfig(), request, response,  filter, fin);
	    }

	    public void destroy() {
	        super.destroy();
	    }
}
