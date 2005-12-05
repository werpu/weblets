package com.apress.projsf.weblets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.java.dev.weblets.WebletConfig;

public class WebletConfigImpl implements WebletConfig
{
  public String getWebletName()
  {
    return _webletName;
  }

  public void setWebletName(String webletName)
  {
    _webletName = webletName;
  }

  public String getWebletClass()
  {
    return _webletClass;
  }

  public void setWebletClass(String webletClass)
  {
    _webletClass = webletClass;
  }

  public String getWebletVersion()
  {
    return _webletVersion;
  }

  public void setWebletVersion(String webletVersion)
  {
    _webletVersion = webletVersion;
  }

  public void addInitParam(String paramName, String paramValue)
  {
    _initParams.put(paramName, paramValue);
  }

  public void addMimeMapping(String extension, String mimeType)
  {
    _mimeMappings.put(extension, mimeType);
  }

  public Iterator getInitParameterNames()
  {
    return _initParams.keySet().iterator();
  }

  public String getInitParameter(String paramName)
  {
    return (String)_initParams.get(paramName);
  }

  public String getMimeType(String resourcePath)
  {
    if (resourcePath != null)
    {
      int lastDot = resourcePath.lastIndexOf('.');
      if (lastDot != -1)
      {
        String extension = resourcePath.substring(lastDot + 1);
        return (String)_mimeMappings.get(extension);
      }
    }
    return null;
  }

  private String _webletName;
  private String _webletClass;
  private String _webletVersion;
  private Map    _initParams = new HashMap(3);
  private Map    _mimeMappings = new HashMap(3);
}
