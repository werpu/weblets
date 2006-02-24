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
