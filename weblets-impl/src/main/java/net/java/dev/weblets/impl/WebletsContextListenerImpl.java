/*
 * WebletContextListener.java
 *
 * Created on November 29, 2006, 12:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.dev.weblets.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import javax.faces.FacesException;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.java.dev.weblets.WebletContainer;
import net.java.dev.weblets.WebletsServlet;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import net.java.dev.weblets.impl.parse.DisconnectedEntityResolver;

/**
 *
 * @author john.fallows
 */
public class WebletsContextListenerImpl implements ServletContextListener {
    
//    public static final String WEBLET_CONTAINER_KEY = WebletContainer.class.getName();
    
    public void contextInitialized(
        ServletContextEvent event) 
    {
        ServletContext context = event.getServletContext();
        WebletContainer container = createContainer(context);
        //throws NPE on Glassfish
        //context.setAttribute(WEBLET_CONTAINER_KEY, container);
    }

    public void contextDestroyed(
        ServletContextEvent event) 
    {
        WebletContainerImpl container = (WebletContainerImpl)WebletContainer.getInstance();
        container.destroy();
    }

    private WebletContainer createContainer(ServletContext context) {
        try {
            URL webXml = context.getResource("/WEB-INF/web.xml");
            
            String facesPattern = "/faces/*";
            
            if (webXml != null) {
                InputStream in = webXml.openStream();
                try {
                    WebXmlParser parser = new WebXmlParser();
                    Digester digester = new Digester();
                    digester.setValidating(false);
                    digester.setEntityResolver(DisconnectedEntityResolver.sharedInstance());
                    digester.push(parser);
                    digester.addCallMethod("web-app/servlet",
                            "addServlet", 2);
                    digester.addCallParam("web-app/servlet/servlet-name", 0);
                    digester.addCallParam("web-app/servlet/servlet-class", 1);
                    digester.addCallMethod("web-app/servlet-mapping",
                            "addServletMapping", 2);
                    digester.addCallParam("web-app/servlet-mapping/servlet-name", 0);
                    digester.addCallParam("web-app/servlet-mapping/url-pattern", 1);
                    digester.parse(in);
                    
                    facesPattern = parser.getFacesPattern();
                    
                    if(!isPathPattern(facesPattern) && parser.isJSFEnabled()) {
                    	Log logger = LogFactory.getLog(this.getClass());
                    	logger.warn("JSF Enabled Weblets but path pattern is missing, some relatively referenced resources might not load");
                    } else if(!isPathPattern(parser.getWebletPattern()) && parser.isServletEnabled()) {
                    	Log logger = LogFactory.getLog(this.getClass());
                    	logger.warn("Servlet Enabled Weblets but path pattern is missing, some relatively referenced resources might not load");
                    }
                } catch (SAXException e) {
                    throw new FacesException(e);
                } finally {
                    in.close();
                }
            }
            
            // TODO: determine Faces Weblets ViewIds, assumes /weblets/*
            String webletsViewIds = "/weblets/*";
            
            // auto-prepend leading slash in case it is missing from web.xml entry
            if(!facesPattern.startsWith("/")) {
                facesPattern = "/" + facesPattern;
            }
            
            String formatPattern = facesPattern.replaceFirst("/\\*", webletsViewIds)
            .replaceFirst("/\\*", "{0}");
            
            String webletsPattern = facesPattern.replaceAll("\\.", "\\\\.")
            .replaceAll("\\*", "weblets(/.*)");
            MessageFormat format = new MessageFormat(formatPattern);
            WebletContainerImpl container = new WebletContainerImpl(context, format, Pattern.compile(webletsPattern));
            try
            {
              URL resource = context.getResource("/WEB-INF/weblets-config.xml");
              if (resource != null)
                container.registerConfig(resource);
            }
            catch (MalformedURLException e)
            {
              context.log("Unabled to register /WEB-INF/weblets-config.xml", e);
            }
            return container;
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }
    
    static public class WebXmlParser {
        public void addServlet(
                String servletName,
                String servletClass) {
            if (FacesServlet.class.getName().equals(servletClass))
                _facesServletName = servletName;
            if (WebletsServlet.class.getName().equals(servletName))
                _webletServletName = servletName;
        }
        
        public void addServletMapping(
                String servletName,
                String urlPattern) {
            if (servletName.equals(_facesServletName))
            	if(_facesPattern == null || _facesPattern.trim().equals("") || isPathPattern(urlPattern))
                _facesPattern = urlPattern;
            if (servletName.equals(_webletServletName))
            	if(_webletPattern == null || _webletPattern.trim().equals("") || isPathPattern(urlPattern))
                _webletPattern = urlPattern;
        }
        
        public boolean isServletEnabled() {
        	return _webletServletName != null && !_webletServletName.trim().equals("");
        }
        
        public boolean isJSFEnabled() {
        	return _facesServletName != null && !_facesServletName.trim().equals("");
        }
        
        public String getFacesPattern() {
            return _facesPattern;
        }
        
        public String getWebletPattern() {
            return _webletPattern;
        }
        
        
        
        private String _facesServletName;
        private String _facesPattern;

        private String _webletServletName;
        private String _webletPattern;
    }
    
    private static boolean isPathPattern(String in) {
    	if(in == null) return false;
		return in.trim().matches("^(.)*\\/.*\\/(\\*){0,1}$");
	}
    
}
