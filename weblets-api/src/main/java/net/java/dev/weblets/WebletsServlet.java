/*
 * Copyright 2005 John R. Fallows
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.java.dev.weblets;

import net.java.dev.weblets.util.ServiceLoader;

import javax.servlet.*;
import java.io.IOException;

/**
 * The WebletsServlet maps requested URLs to Weblet resources.
 * 
 * The <code>META-INF/services/net.java.dev.weblets.WebletsServlet</code> Service Provider configuration file is used to lookup the implementation class for
 * this Servlet, as defined by the JAR file specification.
 */
public final class WebletsServlet implements Servlet {
	/**
	 * Initializes this Servlet.
	 * 
	 * @param config
	 *            the servlet configuration
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			_delegate = (Servlet) _WEBLETS_SERVLET_CLASS.newInstance();
			_delegate.init(config);
		} catch (IllegalAccessException e) {
			throw new ServletException("Unable to access " + "WebletsServlet implementation", e);
		} catch (InstantiationException e) {
			throw new ServletException("Unable to instantiate " + "WebletsServlet implementation", e);
		}
	}

	/**
	 * Destroys this Servlet.
	 */
	public void destroy() {
		_delegate.destroy();
	}

	/**
	 * Returns the configuration for this Servlet.
	 * 
	 * @return the configuration for this Servlet
	 */
	public ServletConfig getServletConfig() {
		return _delegate.getServletConfig();
	}

	/**
	 * Returns the information describing this Servlet.
	 * 
	 * @return the information describing this Servlet
	 */
	public String getServletInfo() {
		return _delegate.getServletInfo();
	}

	/**
	 * Processes the incoming request, by looking up the Weblet mapped to the incoming request URL pattern, and dispatching to the Weblet if found, otherise
	 * sends 404 Not Found response.
	 * 
	 * @param request
	 *            the servlet request
	 * @param response
	 *            the servlet response
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		_delegate.service(request, response);
	}

	private Servlet				_delegate;
	// the WebletsServlet Service Provider implementation class
	static private final Class	_WEBLETS_SERVLET_CLASS;
	static {
		_WEBLETS_SERVLET_CLASS = ServiceLoader.loadService(WebletsServlet.class.getName());
	}
}
