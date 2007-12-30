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

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import net.java.dev.weblets.impl.WebletContainerImpl;
import net.java.dev.weblets.WebletContainer;

public class WebletsFilterImpl implements Filter
{
  public void init(
    FilterConfig config) throws ServletException
  {
    _servletContext = config.getServletContext();
    //_webletContainer = (WebletContainerImpl)config.getServletContext().getAttribute(WebletsContextListenerImpl.WEBLET_CONTAINER_KEY);
    _webletContainer = (WebletContainerImpl)WebletContainer.getInstance();
  }

  public void destroy()
  {
    _webletContainer = null;
  }

  public void doFilter(
    ServletRequest  request,
    ServletResponse response,
    FilterChain     chain) throws IOException, ServletException
  {
    WebletRequest webRequest = null;

    if (request instanceof HttpServletRequest)
    {
      HttpServletRequest httpRequest = (HttpServletRequest)request;
      String contextPath = httpRequest.getContextPath();
      String requestURI = getCanonicalPath(httpRequest.getServletPath());
      long ifModifiedSince = httpRequest.getDateHeader("If-Modified-Since");

      try
      {
        String[] parsed =
          _webletContainer.parseWebletRequest(contextPath, requestURI, ifModifiedSince);

        if (parsed != null)
        {
          String webletName = parsed[0];
          String webletPath = parsed[1];
          String webletPathInfo = parsed[2];
          webRequest = 
            new WebletRequestImpl(webletName, webletPath, contextPath, webletPathInfo, 
                                  ifModifiedSince, httpRequest);
          HttpServletResponse httpResponse = (HttpServletResponse)response;
          httpRequest.setCharacterEncoding("UTF-8");
          String contentName = webRequest.getPathInfo();
          String contentTypeDefault = _servletContext.getMimeType(contentName);
          WebletResponse webResponse =
            new WebletResponseImpl(contentTypeDefault, httpResponse);
          _webletContainer.service(webRequest, webResponse);
        }
      }
      catch (WebletException e)
      {
        throw new ServletException(e);
      }
    }

    if (webRequest == null)
      chain.doFilter(request, response);
  }

  private String getCanonicalPath(
    String path)
  {
    int len;
    do
    {
      len = path.length();
      path = path.replaceAll("/[^/]+/\\.\\.", "");
    } while (path.length() != len);

    return path;
  }


  private ServletContext      _servletContext;
  private WebletContainerImpl _webletContainer;
}
