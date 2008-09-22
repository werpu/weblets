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

import java.util.*;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletContainer;
import net.java.dev.weblets.util.StringUtils;

public class WebletConfigImpl implements WebletConfig {
    public WebletConfigImpl(WebletContainerImpl container) {
        _container = container;
        initAllowedFiletypes();
    }

    public WebletContainer getWebletContainer() {
        return _container;
    }

    public String getWebletName() {
        return _webletName;
    }

    public void setWebletName(String webletName) {
        _webletName = webletName;
    }

    public String getWebletClass() {
        return _webletClass;
    }

    public void setWebletClass(String webletClass) {
        _webletClass = webletClass;
    }

    public String getWebletVersion() {
        return _webletVersion;
    }

    public void setWebletVersion(String webletVersion) {
        _webletVersion = webletVersion;
    }

    public void addInitParam(String paramName, String paramValue) {
        _initParams.put(paramName, paramValue);
    }

    public void addMimeMapping(String extension, String mimeType) {
        _mimeMappings.put(extension, mimeType);
    }

    public Iterator getInitParameterNames() {
        return _initParams.keySet().iterator();
    }

    public String getInitParameter(String paramName) {
        return (String) _initParams.get(paramName);
    }

    public String getMimeType(String resourcePath) {
        if (resourcePath != null) {
            String extension = StringUtils.getExtension(resourcePath);
            if (extension.length() < resourcePath.length()) {
                String retVal = (String) _mimeMappings.get(extension);
                /*no local mimetype we try the servlet context mime type*/
                if (retVal == null) {
                    /**
                     *  we check for the underlying container mimetype if we dont have a valid one
                     *  set via our overrides
                     */
                    retVal = _container.getContainerMimeType(resourcePath);
                }
                return retVal;
            }
        }
        return null;
    }

    public Set getAllowedResources() {
        return _allowedResources;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * lazy initialization code for the allowed filetypes list
     */
    private void initAllowedFiletypes() {
        String allowedFiletypes = this.getInitParameter("allowedResources");
        // we now determine the allowed mime types for this weblet
        if (!StringUtils.isBlank(allowedFiletypes)) {
            String[] filetypesArr = allowedFiletypes.split("[\\,\\;]");
            for (int cnt = 0; cnt < filetypesArr.length; cnt++) {
                String fileType = filetypesArr[cnt];
                fileType = fileType.replaceAll("\\*", "");
                fileType = fileType.replaceAll("\\.", "");
                fileType = fileType.trim().toLowerCase();
                if (fileType.equals("*")) { /* all are allowed */
                    _allowedResources = null;
                    return;
                }
                if (_allowedResources == null) {
                    _allowedResources = new HashSet();
                }
                _allowedResources.add(fileType);
            }
        }
    }

    private Set _allowedResources = null;
    private WebletContainerImpl _container;
    private String _webletName;
    private String _webletClass;
    private String _webletVersion;
    private Map _initParams = new HashMap(3);
    private Map _mimeMappings = new HashMap(3);
}
