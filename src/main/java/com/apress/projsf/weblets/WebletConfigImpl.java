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
