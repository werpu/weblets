package com.apress.projsf.weblets.faces;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;

import net.java.dev.weblets.WebletContainer;

public class WebletsViewHandler extends ViewHandlerWrapper
{
  public WebletsViewHandler(
    ViewHandler delegate)
  {
    super(delegate);
  }

  public String getResourceURL(
    FacesContext context,
    String       path)
  {
    Matcher matcher = WEBLET_PROTOCOL.matcher(path);
    if (matcher.matches())
    {
      String webletName = matcher.group(1);
      String pathInfo   = matcher.group(2);

      WebletContainer container = getWebletContainer();
      path = container.getWebletURL(webletName, pathInfo);
    }

    return super.getResourceURL(context, path);
  }

  private WebletContainer getWebletContainer()
  {
    if (_container == null)
    {
      // TODO: parse WEB-INF/web.xml to determine each mapping, eg.
      //         filter   /projsf-*
      //         servlet  /weblets/*
      //         phase(1) /faces/weblets/*
      //         phase(2) /weblets/*.jsf
      //       lazily create WebletContainer
      //         register all patterns in web.xml
      //       on servlet/filter startup also
      //         lazily create WebletContainer (unless already created)
      //           register all patterns in web.xml
      //       WebletContainerFactory.getWebletContainer(URL webXML)
      _container = WebletContainer.getInstance();
    }

    return _container;
  }

  private WebletContainer _container;

  private static final Pattern WEBLET_PROTOCOL =
                          Pattern.compile("weblet://([^/]+)(/.+)");
}

