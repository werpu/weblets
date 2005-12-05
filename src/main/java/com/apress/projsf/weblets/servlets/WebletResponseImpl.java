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
