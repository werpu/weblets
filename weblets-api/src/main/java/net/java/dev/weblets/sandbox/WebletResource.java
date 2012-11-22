/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package net.java.dev.weblets.sandbox;

import net.java.dev.weblets.Weblet;
import net.java.dev.weblets.WebletContainer;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.WebletUtils;
import net.java.dev.weblets.packaged.ResourceloadingUtils;
import net.java.dev.weblets.util.VersioningUtils;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */

public class WebletResource extends Resource
{

    WebletContainer container;
    Weblet weblet;

    public WebletResource(final String libraryName, String resourceName)
    {
        if (resourceName.startsWith("/"))
        {
            resourceName = resourceName.substring(1);
        }
        setLibraryName(libraryName);
        setResourceName(resourceName);
        container = WebletContainer.getInstance();
        weblet = container.getWeblet(getLibraryName());
        String pattern = "*." + resourceName.substring(resourceName.lastIndexOf(".") + 1);
        setContentType(container.getContainerMimeType(pattern));
    }

    public WebletResource(final String libraryName, String resourceName, String contentType)
    {
        if (resourceName.startsWith("/"))
        {
            resourceName = resourceName.substring(1);
        }
        setLibraryName(libraryName);
        setResourceName(resourceName);
        setContentType(contentType);
        container = WebletContainer.getInstance();
        weblet = container.getWeblet(getLibraryName());
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return WebletUtils.getResourceAsStream(getLibraryName(), "/" + getResourceName(), getContentType());
    }

    @Override
    /**
     * returns the request path jsf
     * style with prefix and postfix mapping
     * utilized accordingly
     *
     * the version which is prefixed in our normal requests
     * is postfixed in standard jsf request paths
     */
    public String getRequestPath()
    {
        final FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String mapping = calculatePrefixMapping(externalContext.getRequestServletPath(),
                externalContext.getRequestPathInfo());
        boolean prefixMapping = true;
        if (mapping == null)
        {
            prefixMapping = false;
            mapping = calculatePostfixMapping(externalContext.getRequestServletPath(),
                    externalContext.getRequestPathInfo());
        }

        if (prefixMapping)
        {
            return context
                    .getApplication()
                    .getViewHandler()
                    .getResourceURL(
                            context, mapping +
                            ResourceHandler.RESOURCE_IDENTIFIER + "/" + getResourceName()
                            + "?ln=" + getLibraryName() + "&vsn=" + weblet.getWebletConfig().getWebletVersion());
        } else
        {
            return context
                    .getApplication()
                    .getViewHandler()
                    .getResourceURL(
                            context,
                            ResourceHandler.RESOURCE_IDENTIFIER + "/" + getResourceName()
                                    + mapping + "?ln=" + getLibraryName() + "&vsn=" + weblet.getWebletConfig().getWebletVersion());
        }
    }

    @Override
    public Map<String, String> getResponseHeaders()
    {
        long timeout = weblet.getWebletConfig().getCacheTimeout();
        timeout = y2038k_fix(timeout);

        long ifModifiedSince = getIfModifiedSince();
        String webletPath = getWebletPath(getResourceName());
        String webletResource = getWebletResource(getResourceName());

        WebletRequest request = ResourceloadingUtils.getInstance().createWebletRequest(getLibraryName(), "",
                FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath(),
                "/" + getResourceName(), ifModifiedSince, FacesContext.getCurrentInstance().getExternalContext().getRequest());
        URL resourceURL = null;
        URLConnection conn = null;
        try
        {
            resourceURL = weblet.getResourceURL(request);
            conn = resourceURL.openConnection();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        long lastModified = conn.getLastModified();
        int contentLength = conn.getContentLength();

        //long lastModified = -1;

        Map<String, String> responseHeader = new HashMap<String, String>();
        //responseHeader.put(WebletResponse.HTTP_CONTENT_LENGTH, String.valueOf(contentLength));

        if (VersioningUtils.isVersionedWeblet(weblet.getWebletConfig().getWebletVersion()))
        {
            if (timeout != -1)
            {
                responseHeader.put(WebletResponse.HTTP_EXPIRES, (new HttpDateFormat().format((Long) timeout)));
                responseHeader.put(WebletResponse.HTTP_CACHE_CONTROL, "pre-check=9000,post-check=9000,max-age=" + ((timeout - System.currentTimeMillis()) / 1000));
            } else
            {
                //versioned weblet we set a future date
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, 2056);
                responseHeader.put(WebletResponse.HTTP_EXPIRES, (new HttpDateFormat().format((Long) cal.getTimeInMillis())));
                responseHeader.put(WebletResponse.HTTP_CACHE_CONTROL, "pre-check=9000,post-check=9000,max-age=" + ((cal.getTimeInMillis() - System.currentTimeMillis()) / 1000));
                responseHeader.put(WebletResponse.HTTP_LAST_MODIFIED, (new HttpDateFormat().format((Long) cal.getTimeInMillis())));
            }
        } else
        {
            responseHeader.put(WebletResponse.HTTP_LAST_MODIFIED, (new HttpDateFormat().format((Long) lastModified)));

            if (timeout != -1)
            {
                responseHeader.put(WebletResponse.HTTP_EXPIRES, (new HttpDateFormat().format((Long) timeout)));
            }
        }

        return responseHeader;
    }

    private String getWebletResource(String resourceName)
    {
        int pathEnd = resourceName.lastIndexOf("/");
        if (pathEnd == -1 || pathEnd == 0) return resourceName;
        return resourceName.substring(pathEnd);

    }

    private String getWebletPath(String resourceName)
    {
        int pathEnd = resourceName.lastIndexOf("/");
        if (pathEnd == -1) return "";
        return resourceName.substring(0, pathEnd);
    }

    private long getIfModifiedSince()
    {
        String ifModifiedHeader = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("If-Modified-Since");
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
        return ifModifiedSince;
    }

    String calculatePrefixMapping(String requestServletPath, String requestPathInfo)
    {
        if (requestPathInfo == null) return null;
        return requestServletPath;
    }

    String calculatePostfixMapping(String requestServletPath, String requestPathInfo)
    {
        int slashPos = requestServletPath.lastIndexOf('/');
        int extensionPos = requestServletPath.lastIndexOf('.');
        if (extensionPos > -1 && extensionPos > slashPos)
        {
            String extension = requestServletPath.substring(extensionPos);
            return extension;
        }
        return null;
    }

    private static final int Y0238_VALIDYEAR = 2037;
    private static final int Y2038_BOUNDARY = 2038;

    /**
     * y2038k fix for certain servers (aka WAS 6.1)
     *
     * @param timeout the incoming time value to be fixed
     * @return
     */
    private long y2038k_fix(long timeout)
    {
        java.util.Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeout);
        if (cal.get(Calendar.YEAR) >= Y2038_BOUNDARY)
        {
            cal.set(Calendar.YEAR, Y0238_VALIDYEAR);
        }
        return cal.getTimeInMillis();
    }

    @Override
    public URL getURL()
    {
        return null;
    }

    @Override
    public boolean userAgentNeedsUpdate(final FacesContext context)
    {
        return true;
    }

    class HttpDateFormat extends SimpleDateFormat
    {
        /**
         * Creates a new HttpDateFormat.
         */
        public HttpDateFormat()
        {
            super("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
            // match the pattern exactly, or fail to parse
            setLenient(false);
        }

        /**
         * The serialization version.
         */
        private static final long serialVersionUID = 1L;
    }
}
