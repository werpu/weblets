package net.java.dev.weblets;

import java.io.IOException;

abstract public class Weblet
{
  public void init(WebletConfig config)
  {
    _config = config;
  }

  public void destroy()
  {
    _config = null;
  }

  public WebletConfig getWebletConfig()
  {
    return _config;
  }

  abstract public void service(
    WebletRequest  request,
    WebletResponse response) throws IOException, WebletException;

  private WebletConfig _config;
}