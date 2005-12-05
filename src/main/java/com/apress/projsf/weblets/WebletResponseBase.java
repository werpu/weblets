package com.apress.projsf.weblets;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import net.java.dev.weblets.WebletResponse;

abstract public class WebletResponseBase implements WebletResponse
{
  public WebletResponseBase(
    String contentTypeDefault)
  {
    _contentTypeDefault = contentTypeDefault;
  }

  public final void setDefaultContentType(
    String contentTypeDefault)
  {
    _contentTypeDefault = contentTypeDefault;
  }

  public final String getDefaultContentType()
  {
    return _contentTypeDefault;
  }

  public final void setContentType(
    String contentType)
  {
    if (contentType == null)
      contentType = _contentTypeDefault;
    setContentTypeImpl(contentType);
  }

  public final void setLastModified(
    long lastModified)
  {
    // Detect unknown-last-modified
    if (lastModified != 0)
      setLastModifiedImpl(lastModified);
  }

  public final void setContentVersion(
    String contentVersion)
  {
    if (contentVersion != null  &&
        !contentVersion.endsWith("-SNAPSHOT"))
    {
      setContentVersionImpl(contentVersion);
    }
  }

  abstract protected void setContentTypeImpl(
    String contentType);

  abstract protected void setLastModifiedImpl(
    long lastModified);

  abstract protected void setContentVersionImpl(
    String contentVersion);

  private String _contentTypeDefault;
}
