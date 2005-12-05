package net.java.dev.weblets;

import java.io.IOException;
import java.io.OutputStream;

public interface WebletResponse
{
  static public final int SC_ACCEPTED  = 0;
  static public final int SC_NOT_FOUND = 1;

  public void setDefaultContentType(String contentTypeDefault);
  public String getDefaultContentType();

  public void setLastModified(long lastModified);
  public void setContentType(String contentType);
  public void setContentLength(int contentLength);
  public void setContentVersion(String contentVersion);

  public OutputStream getOutputStream() throws IOException;

  public void setStatus(int status);
}