package net.java.dev.weblets;

import java.util.Iterator;

public interface WebletConfig
{
  public String   getWebletName();
  public String   getWebletVersion();
  public String   getInitParameter(String paramName);
  public Iterator getInitParameterNames();
  public String   getMimeType(String resourcePath);
}