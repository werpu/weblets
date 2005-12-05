package com.apress.projsf.weblets.faces;

import java.io.IOException;

import java.text.MessageFormat;
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

public class WebletsPhaseListener implements PhaseListener
{
  public WebletsPhaseListener()
  {
    // TODO: determine FacesServlet mapping, assumes /faces/*
    String facesPattern = "/faces/*"; // "/*.jsf";
    // TODO: determine Faces Weblets ViewIds, assumes /weblets/*
    String webletsViewIds = "/weblets/*";
    String formatPattern = facesPattern.replaceFirst("/\\*", webletsViewIds)
                                       .replaceFirst("/\\*", "{0}");
    MessageFormat format = new MessageFormat(formatPattern);
    _webletContainer = new WebletContainerImpl(format);
    //_webletsPattern = Pattern.compile("/weblets(/.*)\\.jsf");
    _webletsPattern = Pattern.compile("/faces/weblets(/.*)");
  }

  public void afterPhase(
    PhaseEvent event)
  {
  }

  public void beforePhase(
    PhaseEvent event)
  {
    FacesContext context = FacesContext.getCurrentInstance();
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
      long ifModifiedSince =
            (ifModifiedHeader != null) ? Long.parseLong(ifModifiedHeader) : -1L;

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

  private WebletContainerImpl _webletContainer;
  private Pattern _webletsPattern;
}