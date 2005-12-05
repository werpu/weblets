package com.apress.projsf.weblets;

import net.java.dev.weblets.WebletRequest;

public class WebletRequestImpl implements WebletRequest
{
  public WebletRequestImpl(
    String webletName,
    String webletPath,
    String contextPath,
    String pathInfo,
    long   ifModifiedSince)
  {
    _webletName = webletName;
    _webletPath = webletPath;
    _contextPath = contextPath;
    _pathInfo = pathInfo;
    _ifModifiedSince = ifModifiedSince;
  }

  public String getWebletName()
  {
    return _webletName;
  }

  public String getWebletPath()
  {
    return _webletPath;
  }

  public String getContextPath()
  {
    return _contextPath;
  }

  public String getPathInfo()
  {
    return _pathInfo;
  }

  public long getIfModifiedSince()
  {
    return _ifModifiedSince;
  }

  private final String _webletName;
  private final String _webletPath;
  private final String _contextPath;
  private final String _pathInfo;
  private final long   _ifModifiedSince;
}
