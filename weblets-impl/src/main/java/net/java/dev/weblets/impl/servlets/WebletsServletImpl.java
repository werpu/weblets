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
package net.java.dev.weblets.impl.servlets;

import net.java.dev.weblets.WebletContainer;
import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.impl.WebletContainerImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebletsServletImpl extends HttpServlet {
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// _webletContainer = (WebletContainerImpl)getServletContext().getAttribute(WebletsContextListenerImpl.WEBLET_CONTAINER_KEY);
		_webletContainer = (WebletContainerImpl) WebletContainer.getInstance();
	}

	public void destroy() {
		_webletContainer = null;
	}

	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
		String contextPath = httpRequest.getContextPath();
		String requestURI = getCanonicalPath(httpRequest.getPathInfo());
		long ifModifiedSince = httpRequest.getDateHeader("If-Modified-Since");
		try {
			String[] parsed = _webletContainer.parseWebletRequest(contextPath, requestURI, ifModifiedSince);
			if (parsed != null) {
				String webletName = parsed[0];
				String webletPath = parsed[1];
				String webletPathInfo = parsed[2];
				WebletRequest webRequest = new WebletRequestImpl(webletName, webletPath, contextPath, webletPathInfo, ifModifiedSince, httpRequest);
				String contentName = webRequest.getPathInfo();
				String contentTypeDefault = getServletContext().getMimeType(contentName);
				WebletResponse webResponse = new WebletResponseImpl(contentTypeDefault, httpResponse);
				_webletContainer.service(webRequest, webResponse);
			}
		} catch (WebletException e) {
			throw new ServletException(e);
		}
	}

	private String getCanonicalPath(String path) {
		int len;
		do {
			len = path.length();
			path = path.replaceAll("/[^/]+/\\.\\.", "");
		} while (path.length() != len);
		return path;
	}

	private WebletContainerImpl	_webletContainer;
	/**
	 * The serialization version.
	 */
	private static final long	serialVersionUID	= 1L;
}
