package com.apress.projsf.weblets.servlets;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
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

import com.apress.projsf.weblets.WebletContainerImpl;

public class WebletsFilterImpl implements Filter
{
  public void init(
    FilterConfig config) throws ServletException
  {
    _servletContext = config.getServletContext();
    _webletContainer = createWebletContainer(config.getServletContext());
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
        webRequest = _webletContainer.getWebletRequest(contextPath,
                                                       requestURI,
                                                       ifModifiedSince);
        if (webRequest != null)
        {
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

  private WebletContainerImpl createWebletContainer(
    ServletContext context)
  {
    // TODO: determine WebletsFilter mapping, assumes /*
    String webletsPattern = "/*";
    String formatPattern = webletsPattern.replaceFirst("/\\*", "{0}");
    MessageFormat format = new MessageFormat(formatPattern);
    WebletContainerImpl webletContainer = new WebletContainerImpl(format);
    try
    {
      URL resource = context.getResource("/WEB-INF/weblets-config.xml");
      if (resource != null)
        webletContainer.registerConfig(resource);
    }
    catch (MalformedURLException e)
    {
      context.log("Unabled to register /WEB-INF/weblets-config.xml", e);
    }
    return webletContainer;
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
