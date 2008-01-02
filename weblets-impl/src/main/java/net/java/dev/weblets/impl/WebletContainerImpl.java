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
package net.java.dev.weblets.impl;

import net.java.dev.weblets.*;
import net.java.dev.weblets.impl.parse.DisconnectedEntityResolver;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.Format;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebletContainerImpl extends WebletContainer {
    public WebletContainerImpl(
            ServletContext servletContext,
            String webletContextPath,
            Format webletURLFormat,
            Pattern webletURLPattern) throws WebletException {
        _servletContext = servletContext;
        _webletURLFormat = webletURLFormat;
        _webletURLPattern = webletURLPattern;
        _webletContextPath = webletContextPath;

        checkWebletsContextPath();


        try

        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Enumeration e = loader.getResources("META-INF/weblets-config.xml");

            // Defensive: Glassfish.v2.b25 produces duplicates in Enumeration
            //            returned by loader.getResources()
            Set urls = new HashSet();
            while (e.hasMoreElements()) {
                urls.add(e.nextElement());
            }

            for (Iterator i = urls.iterator(); i.hasNext();) {
                URL resource = (URL) i.next();
                registerConfig(resource);
            }

            WebletContainer.setInstance(this);
        }
        catch (IOException e) {
            throw new WebletException(e);
        }
    }

    private void checkWebletsContextPath() {
        if (_webletContextPath == null || _webletContextPath.trim().equals("")) {
            //the setup context path defaults to empty lets determine another one if possible
            Log log = LogFactory.getLog(WebletContainerImpl.class);
            log.warn("No net.java.dev.weblets.contextpath context-param has been set" +
                    " this might cause problems in non jsf environments! ");
        } else {
            if (_webletContextPath.endsWith("/"))
                _webletContextPath = _webletContextPath.substring(0, _webletContextPath.length() - 1);
        }
    }

    public ServletContext getServletContext() {
        return _servletContext;
    }

    public void destroy() {
        Iterator i = _weblets.values().iterator();
        while (i.hasNext()) {
            Weblet weblet = (Weblet) i.next();
            weblet.destroy();
        }
        _weblets = null;
        _webletConfigs = null;
        _webletMappings = null;
        _webletContextPath = null;
    }

    public Pattern getPattern() {
        return _webletURLPattern;
    }

    public String[] parseWebletRequest(
            String contextPath,
            String requestURI,
            long ifModifiedSince) {
        Iterator i = _webletMappings.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            Pattern pattern = (Pattern) entry.getValue();
            Matcher matcher = pattern.matcher(requestURI);
            if (matcher.matches()) {
                String webletPath = matcher.group(1);
                String pathInfo = matcher.group(2);
                String webletName = (String) entry.getKey();
                return new String[]{webletName, webletPath, pathInfo};
            }
        }

        return null;
    }

    /*setter and getter to access the context path*/
    public String getWebletContextPath() {
       return _webletContextPath;
    }
    public void setWebletContextPath(String contextPath) {
        _webletContextPath = contextPath;
    }

    public void service(
            WebletRequest request,
            WebletResponse response) throws IOException, WebletException {
        String webletName = request.getWebletName();

        Weblet weblet = (Weblet) _weblets.get(webletName);

        if (weblet == null) {
            try {
                WebletConfigImpl config = (WebletConfigImpl) _webletConfigs.get(webletName);
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                Class klass = loader.loadClass(config.getWebletClass());
                weblet = (Weblet) klass.newInstance();
                weblet.init(config);
                _weblets.put(webletName, weblet);
            }
            catch (ClassNotFoundException e) {
                throw new WebletException(e);
            }
            catch (InstantiationException e) {
                throw new WebletException(e);
            }
            catch (IllegalAccessException e) {
                throw new WebletException(e);
            }
        }

        if (response.getDefaultContentType() == null) {
            String pathInfo = request.getPathInfo();
            WebletConfig webConfig = weblet.getWebletConfig();
            if (pathInfo != null) {
                String mimeType = webConfig.getMimeType(pathInfo);
                response.setDefaultContentType(mimeType);
            }
        }

        weblet.service(request, response);
    }

    public String getWebletURL(
            String webletName,
            String pathInfo) throws WebletException {
        WebletConfig config = (WebletConfig) _webletConfigs.get(webletName);
        if (config == null)
            throw new WebletException("Missing Weblet configuration for '" + webletName + "'");

        String webletPath = (String) _webletPaths.get(webletName);
        if (webletPath == null)
            throw new WebletException("Missing Weblet mapping for '" + webletName + "'");

        String webletVersion = config.getWebletVersion();

        // URL-syntax  /webletPath[$version]/pathInfo
        StringBuffer buffer = new StringBuffer();
        buffer.append(webletPath);
        if (webletVersion != null) {
            buffer.append('$');
            buffer.append(webletVersion);
        }
        if (pathInfo != null) {
            buffer.append(pathInfo);
        }
        String webletURL = buffer.toString();

        if (_webletURLFormat != null)
            webletURL = _webletURLFormat.format(new Object[]{webletURL});

        return webletURL;
    }

    public void registerConfig(
            URL webletsConfig) {
        try {
            InputStream in = webletsConfig.openStream();
            try {
                Digester digester = new Digester();
                digester.setValidating(false);
                digester.setEntityResolver(DisconnectedEntityResolver.sharedInstance());
                digester.push(this);
                digester.addFactoryCreate("weblets-config/weblet", WEBLET_CONFIG_FACTORY);
                digester.addSetNext("weblets-config/weblet", "addWeblet", WebletConfigImpl.class.getName());
                digester.addCallMethod("weblets-config/weblet/weblet-name",
                        "setWebletName", 0);
                digester.addCallMethod("weblets-config/weblet/weblet-class",
                        "setWebletClass", 0);
                digester.addCallMethod("weblets-config/weblet/weblet-version",
                        "setWebletVersion", 0);
                digester.addCallMethod("weblets-config/weblet/init-param",
                        "addInitParam", 2);
                digester.addCallParam("weblets-config/weblet/init-param/param-name", 0);
                digester.addCallParam("weblets-config/weblet/init-param/param-value", 1);
                digester.addCallMethod("weblets-config/weblet/mime-mapping",
                        "addMimeMapping", 2);
                digester.addCallParam("weblets-config/weblet/mime-mapping/extension", 0);
                digester.addCallParam("weblets-config/weblet/mime-mapping/mime-type", 1);
                digester.addCallMethod("weblets-config/weblet-mapping",
                        "setWebletMapping", 2);
                digester.addCallParam("weblets-config/weblet-mapping/weblet-name", 0);
                digester.addCallParam("weblets-config/weblet-mapping/url-pattern", 1);
                digester.parse(in);
            }
            finally {
                in.close();
            }
        }
        catch (IOException e) {
            throw new WebletException(e);
        }
        catch (SAXException e) {
            throw new WebletException(e);
        }
    }

    public void addWeblet(WebletConfigImpl webletConfig) {
        _webletConfigs.put(webletConfig.getWebletName(), webletConfig);
    }

    public void setWebletMapping(
            String webletName,
            String urlPattern) {
        WebletConfig webletConfig = (WebletConfig) _webletConfigs.get(webletName);

        if (webletConfig == null)
            throw new WebletException("Weblet configuration not found: " + webletName);

        Matcher matcher = _WEBLET_PATH_PATTERN.matcher(urlPattern);
        if (matcher.matches()) {
            String webletVersion = webletConfig.getWebletVersion();
            String webletPath = matcher.group(1);

            StringBuffer buffer = new StringBuffer();
            buffer.append("(\\Q");
            buffer.append(webletPath);
            buffer.append("\\E)");
            if (webletVersion != null) {
                buffer.append("\\Q$");
                buffer.append(webletVersion);
                buffer.append("\\E");
            }
            buffer.append("(/.*)?");
            ;

            _webletMappings.put(webletName, Pattern.compile(buffer.toString()));
            _webletPaths.put(webletName, webletPath);
        } else {
            throw new IllegalArgumentException("Invalid weblet mapping: " + urlPattern);
        }
    }

    private Format _webletURLFormat;
    private String _webletContextPath;
    private Pattern _webletURLPattern;
    private ServletContext _servletContext;
    private Map _weblets = new HashMap();
    private Map _webletPaths = new HashMap();
    private Map _webletConfigs = new HashMap();
    private Map _webletMappings = new LinkedHashMap();

    private ObjectCreationFactory WEBLET_CONFIG_FACTORY = new ObjectCreationFactory() {

        public Object createObject(Attributes attributes) throws Exception {
            return new WebletConfigImpl(WebletContainerImpl.this);
        }

        public Digester getDigester() {
            return digester;
        }

        public void setDigester(Digester digester) {
            this.digester = digester;
        }

        private Digester digester;
    };

    static private final Pattern _WEBLET_PATH_PATTERN =
            Pattern.compile("(/[^\\*]+)?/\\*");
}