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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import com.apress.projsf.weblets.WebletContainerImpl;
import com.apress.projsf.weblets.servlets.WebletResponseImpl;

import javax.faces.webapp.FacesServlet;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class WebletsPhaseListener implements PhaseListener
{
  public void afterPhase(
    PhaseEvent event)
  {
  }

  public void beforePhase(
    PhaseEvent event)
  {
    FacesContext context = FacesContext.getCurrentInstance();

    _initializeLazily(context);

    ExternalContext external = context.getExternalContext();
    String pathInfo = external.getRequestServletPath();
    if (external.getRequestPathInfo() != null)
      pathInfo += external.getRequestPathInfo();

    Matcher matcher = _webletsPattern.matcher(pathInfo);
    if (matcher.matches())
    {
      Map requestHeaders = external.getRequestHeaderMap();
      String contextPath = external.getRequestContextPath();
      String requestURI = matcher.group(1);
      String ifModifiedHeader = (String)requestHeaders.get("If-Modified-Since");

      long ifModifiedSince = -1L;

      if (ifModifiedHeader != null)
      {
        try
        {
          DateFormat rfc1123 = new HttpDateFormat();
          Date parsed = rfc1123.parse(ifModifiedHeader);
          ifModifiedSince = parsed.getTime();
        }
        catch (ParseException e)
        {
          throw new FacesException(e);
        }
      }

      try
      {
        WebletRequest webRequest =
          _webletContainer.getWebletRequest(contextPath, requestURI,
                                            ifModifiedSince);

        if (webRequest != null)
        {
          ServletContext servletContext = (ServletContext)external.getContext();
          HttpServletResponse httpResponse = (HttpServletResponse)external.getResponse();
          String contentName = webRequest.getPathInfo();
          String contentTypeDefault = servletContext.getMimeType(contentName);
          WebletResponse webResponse =
            new WebletResponseImpl(contentTypeDefault, httpResponse);
          _webletContainer.service(webRequest, webResponse);
          context.responseComplete();
        }
      }
      catch (IOException e)
      {
        throw new FacesException(e);
      }
    }
  }

  public PhaseId getPhaseId()
  {
    return PhaseId.RESTORE_VIEW;
  }

  private void _initializeLazily(
    FacesContext context)
  {
    if (!_initialized)
    {
      _initialize(context);
      _initialized = true;
    }
  }

  private void _initialize(
    FacesContext context)
  {
    try
    {
      ExternalContext external = context.getExternalContext();
      URL webXml = external.getResource("/WEB-INF/web.xml");

      String facesPattern = "/faces/*";

      if (webXml != null)
      {
        InputStream in = webXml.openStream();
        try
        {
          WebXmlParser parser = new WebXmlParser();
          Digester digester = new Digester();
          digester.setValidating(false);
          digester.push(parser);
          digester.addCallMethod("web-app/servlet",
                                 "addServlet", 2);
          digester.addCallParam("web-app/servlet/servlet-name", 0);
          digester.addCallParam("web-app/servlet/servlet-class", 1);
          digester.addCallMethod("web-app/servlet-mapping",
                                 "addServletMapping", 2);
          digester.addCallParam("web-app/servlet-mapping/servlet-name", 0);
          digester.addCallParam("web-app/servlet-mapping/url-pattern", 1);
          digester.parse(in);

          facesPattern = parser.getFacesPattern();
        }
        catch (SAXException e)
        {
          throw new FacesException(e);
        }
        finally
        {
          in.close();
        }
      }

      // TODO: determine Faces Weblets ViewIds, assumes /weblets/*
      String webletsViewIds = "/weblets/*";

      // auto-prepend leading slash in case it is missing from web.xml entry
      if(!facesPattern.startsWith("/"))
      {
        facesPattern = "/" + facesPattern;
      }

      String formatPattern = facesPattern.replaceFirst("/\\*", webletsViewIds)
                                         .replaceFirst("/\\*", "{0}");

      MessageFormat format = new MessageFormat(formatPattern);
      _webletContainer = new WebletContainerImpl(format);
      String webletsPattern = facesPattern.replaceAll("\\.", "\\\\.")
                                          .replaceAll("\\*", "weblets(/.*)");
      _webletsPattern = Pattern.compile(webletsPattern);
    }
    catch (IOException e)
    {
      throw new FacesException(e);
    }
  }

  static public class WebXmlParser
  {
    public void addServlet(
      String servletName,
      String servletClass)
    {
      if (FacesServlet.class.getName().equals(servletClass))
        _facesServletName = servletName;
    }

    public void addServletMapping(
      String servletName,
      String urlPattern)
    {
      if (servletName.equals(_facesServletName))
        _facesPattern = urlPattern;
    }

    public String getFacesPattern()
    {
      return _facesPattern;
    }

    private String _facesServletName;
    private String _facesPattern;
  }

  private boolean _initialized;

  private WebletContainerImpl _webletContainer;
  private Pattern _webletsPattern;

  /**
   * The serialization version. 
   */
  private static final long serialVersionUID = -8385571916376473831L;
}