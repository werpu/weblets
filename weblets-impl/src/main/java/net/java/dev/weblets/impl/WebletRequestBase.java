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
package net.java.dev.weblets.impl;

import net.java.dev.weblets.WebletRequest;

public class WebletRequestBase implements WebletRequest
{
  public WebletRequestBase(
    String webletName,
    String webletPath,
    String contextPath,
    String pathInfo,
    long   ifModifiedSince
  )
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
  
  public String getParameter(String name)
  {
      return null;
  }

     public boolean isCompressionEnabled() {
        return _compressionEnabled;
    }

    public String getPreferredCompressionType() {
        return _preferredCompressionType;
    }

    public String[] getAllowedCompressionTypes() {
        return _allowedCompressionTypes;
    }  

  private final String _webletName;
  private final String _webletPath;
  private final String _contextPath;
  private final String _pathInfo;
  private final long   _ifModifiedSince;

  /*compression settings, optional not used in the base class*/
  protected  boolean _compressionEnabled = false;
  protected  String _preferredCompressionType = "";
  protected  String [] _allowedCompressionTypes = new String[0];
}