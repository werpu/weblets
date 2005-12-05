package com.apress.projsf.weblets.servlets;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import com.apress.projsf.weblets.WebletContainerImpl;

public class WebletsServletImpl extends HttpServlet
{
  public void init(
    ServletConfig config) throws ServletException
  {
    super.init(config);
    _webletContainer = createWebletContainer(getServletContext());
  }

  public void destroy()
  {
    _webletContainer = null;
  }

  protected void doGet(
    HttpServletRequest  httpRequest,
    HttpServletResponse httpResponse) throws IOException, ServletException
  {
    String contextPath = httpRequest.getContextPath();
    String requestURI = getCanonicalPath(httpRequest.getPathInfo());
    long ifModifiedSince = httpRequest.getDateHeader("If-Modified-Since");

    try
    {
      WebletRequest webRequest =
        _webletContainer.getWebletRequest(contextPath, requestURI, ifModifiedSince);

      if (webRequest != null)
      {
        ServletContext servletContext = getServletContext();
        String contentName = webRequest.getPathInfo();
        String contentTypeDefault = servletContext.getMimeType(contentName);
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

  private WebletContainerImpl createWebletContainer(
    ServletContext context)
  {
    // TODO: determine WebletsServlet mapping, assumes /weblets/*
    String webletsPattern = "/weblets/*";
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

  private WebletContainerImpl _webletContainer;
}
