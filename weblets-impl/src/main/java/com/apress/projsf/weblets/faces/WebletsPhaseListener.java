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

import com.apress.projsf.weblets.WebletContainerImpl;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;

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
import com.apress.projsf.weblets.servlets.WebletRequestImpl;
import com.apress.projsf.weblets.servlets.WebletResponseImpl;
import javax.servlet.http.HttpServletRequest;
import net.java.dev.weblets.WebletContainer;

public class WebletsPhaseListener implements PhaseListener {
    
    private static ThreadLocal reentry = 
        new ThreadLocal()
        {
            protected Object initialValue() 
            {
                return Boolean.FALSE;
            }
        };

    public void afterPhase(
            PhaseEvent event) {
    }
    
    public void beforePhase(
            PhaseEvent event) 
    {
        Boolean isReentry = (Boolean)reentry.get();

        try 
        {
            if (isReentry == Boolean.FALSE) 
            {
                reentry.set(Boolean.TRUE);
                doBeforePhase(event);
            }
        }
        finally
        {
            reentry.set(isReentry);
        }
    }

    protected void doBeforePhase(
            PhaseEvent event) 
    {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext external = context.getExternalContext();

        Map applicationScope = external.getApplicationMap();
        //WebletContainerImpl container = (WebletContainerImpl)applicationScope.get(WebletsContextListenerImpl.WEBLET_CONTAINER_KEY);
        WebletContainerImpl container = (WebletContainerImpl)WebletContainer.getInstance();

        String pathInfo = external.getRequestServletPath();
        if (external.getRequestPathInfo() != null)
            pathInfo += external.getRequestPathInfo();

        Matcher matcher = container.getPattern().matcher(pathInfo);
        if (matcher.matches()) {
            Map requestHeaders = external.getRequestHeaderMap();
            String contextPath = external.getRequestContextPath();
            String requestURI = matcher.group(1);
            String ifModifiedHeader = (String)requestHeaders.get("If-Modified-Since");

            long ifModifiedSince = -1L;

            if (ifModifiedHeader != null) {
                try {
                    DateFormat rfc1123 = new HttpDateFormat();
                    Date parsed = rfc1123.parse(ifModifiedHeader);
                    ifModifiedSince = parsed.getTime();
                } catch (ParseException e) {
                    throw new FacesException(e);
                }
            }

            try {
                String[] parsed =
                        container.parseWebletRequest(contextPath, requestURI, ifModifiedSince);

                if (parsed != null) {
                    ServletContext servletContext = (ServletContext)external.getContext();
                    HttpServletRequest httpRequest = (HttpServletRequest)external.getRequest();
                    HttpServletResponse httpResponse = (HttpServletResponse)external.getResponse();

                    String webletName = parsed[0];
                    String webletPath = parsed[1];
                    String webletPathInfo = parsed[2];
                    WebletRequest webRequest =
                            new WebletRequestImpl(webletName, webletPath, contextPath, webletPathInfo,
                            ifModifiedSince, httpRequest);
                    String contentName = webRequest.getPathInfo();
                    String contentTypeDefault = servletContext.getMimeType(contentName);
                    WebletResponse webResponse =
                            new WebletResponseImpl(contentTypeDefault, httpResponse);
                    container.service(webRequest, webResponse);
                    context.responseComplete();
                }
            } catch (IOException e) {
                throw new FacesException(e);
            }
        }
    }
    
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
    /**
     * The serialization version.
     */
    private static final long serialVersionUID = -8385571916376473831L;
}