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
package net.java.dev.weblets.impl.servlets;

import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.packaged.WebletResourceloadingUtils;
import net.java.dev.weblets.impl.WebletResponseBase;
import net.java.dev.weblets.impl.misc.ReflectUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Weblets Response impl object
 * implementation of the internal
 * weblets response object
 * <p/>
 * TODO check out how to enable all the header
 * params in portlet environments which do not provide
 * the needed methods but follow the pure ri interfaces
 */
public class WebletResponseImpl extends WebletResponseBase {
    public WebletResponseImpl(
            String contentTypeDefault,
            ServletResponse httpResponse) {
        super(contentTypeDefault);
        //new GZIPResponseWrapper(
        _httpResponse = httpResponse;
    }

    public void setContentLength(
            int length) {
        _httpResponse.setContentLength(length);
    }

    public OutputStream getOutputStream() throws IOException {
        //return _httpResponse.getOutputStream();
        return ReflectUtils.getOutputStream(getHttpResponse());
    }

    public void setStatus(int statusCode) {
        switch (statusCode) {
            case WebletResponse.SC_ACCEPTED:
                setResponseStatus(HttpServletResponse.SC_ACCEPTED);
                break;
            case WebletResponse.SC_NOT_FOUND:
                setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
                break;
            case WebletResponse.SC_NOT_MODIFIED:
                setResponseStatus(HttpServletResponse.SC_NOT_MODIFIED);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public ServletResponse getHttpResponse
            () {
        return _httpResponse;
    }

    protected void setContentTypeImpl
            (
                    String
                            contentType) {
        _httpResponse.setContentType(contentType);
    }

    protected void setLastModifiedImpl
            (
                    long lastModified) {
        setDateHeader("Last-Modified", lastModified);
    }

    protected void setContentVersionImpl(String contentVersion, long timeout) {
        setDateHeader("Expires", timeout);
    }

    /**
     * locking singleton to prevent too many warnings in certain portlet environments
     * which dont have the needed methods implemented  (which follow the pure ri)
     */
    static boolean dateheader_warn = false;

    /**
     * we fallback to various introspection methods to support portlet environments
     * as well
     *
     * @param entry
     * @param lastModified
     */
    private void setDateHeader(String entry, long lastModified) {
        Method[] supportedMethods = _httpResponse.getClass().getMethods();
        //fetch the date header method
        Method m = null;
        try {

            m = _httpResponse.getClass().getMethod("setDateHeader", new Class[]{String.class, long.class});
        } catch (NoSuchMethodException e) {
            if (!dateheader_warn) {

                dateheader_warn = true; //we do not want to flood warnings one is enough
                Log log = LogFactory.getLog(getClass());
                log.warn("No date header setting possible reason: ");
                log.warn(e);
                return;
            }
        }

        try {
            Object[] params = new Object[2];
            params[0] = entry;
            params[1] = new Long(lastModified);
            m.invoke(_httpResponse, params);
        } catch (IllegalAccessException e) {

            Log log = LogFactory.getLog(getClass());
            log.error(e);
        } catch (InvocationTargetException e) {
            Log log = LogFactory.getLog(getClass());
            log.error(e);
        }
        return;
    }

    /**
     * locking singleton to prevent too many warnings in certain portlet environments
     * which dont have the needed methods implemented  (which follow the pure ri)
     */
    static boolean responsestatus_warn = false;

    private void setResponseStatus
            (
                    int status) {
        Method[] supportedMethods = _httpResponse.getClass().getMethods();
        //fetch the date header method
        Method m = null;
        try {
            m = _httpResponse.getClass().getMethod("setStatus", new Class[]{int.class});
        } catch (NoSuchMethodException e) {
            if (!responsestatus_warn) {
                responsestatus_warn = true;
                Log log = LogFactory.getLog(getClass());
                log.warn("No satus setting possible reason: ");
                log.warn(e);
                return;
            }
        }

        try {
            Object[] params = new Object[1];
            params[0] = new Integer(status);

            m.invoke(_httpResponse, params);
        } catch (IllegalAccessException e) {

            Log log = LogFactory.getLog(getClass());
            log.error(e);
        } catch (InvocationTargetException e) {
            Log log = LogFactory.getLog(getClass());
            log.error(e);
        }
        return;
    }

    ServletResponse _httpResponse = null;

}
