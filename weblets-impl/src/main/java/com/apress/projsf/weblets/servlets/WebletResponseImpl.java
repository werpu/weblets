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
package com.apress.projsf.weblets.servlets;

import com.apress.projsf.weblets.WebletResponseBase;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import net.java.dev.weblets.WebletResponse;

public class WebletResponseImpl extends WebletResponseBase
{
  public WebletResponseImpl(
    String              contentTypeDefault,
    HttpServletResponse httpResponse)
  {
    super(contentTypeDefault);
    _httpResponse = httpResponse;
  }

  public void setContentLength(
    int length)
  {
    _httpResponse.setContentLength(length);
  }

  public OutputStream getOutputStream() throws IOException
  {
    return _httpResponse.getOutputStream();
  }

  public void setStatus(
    int statusCode)
  {
    switch (statusCode)
    {
      case WebletResponse.SC_ACCEPTED:
        _httpResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
        break;
      case WebletResponse.SC_NOT_FOUND:
        _httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        break;
      case WebletResponse.SC_NOT_MODIFIED:
        _httpResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      default:
        throw new IllegalArgumentException();
    }
  }

  protected void setContentTypeImpl(
    String contentType)
  {
    _httpResponse.setContentType(contentType);
  }

  protected void setLastModifiedImpl(
    long lastModified)
  {
    _httpResponse.setDateHeader("Last-Modified", lastModified);
  }

  protected void setContentVersionImpl(
    String contentVersion)
  {
    long now = System.currentTimeMillis();
    long never = now + 1000 * 60 * 60 * 24 * 365;
    _httpResponse.setDateHeader("Expires", never);
  }

  private final HttpServletResponse _httpResponse;
}
