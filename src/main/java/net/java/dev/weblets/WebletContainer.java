package net.java.dev.weblets;

import java.io.IOException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

abstract public class WebletContainer
{
  static protected void setInstance(
    WebletContainer container) throws WebletException
  {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    _INSTANCES.put(loader, container);
  }

  static public WebletContainer getInstance() throws WebletException
  {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    return (WebletContainer)_INSTANCES.get(loader);
  }
  
  abstract public void service(
    WebletRequest  request,
    WebletResponse response) throws IOException, WebletException;

  abstract public String getWebletURL(
    String webletName,
    String pathInfo) throws WebletException;

  static private final Map _INSTANCES = 
                              Collections.synchronizedMap(new HashMap());
}
