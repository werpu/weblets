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

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletContainer;
import net.java.dev.weblets.impl.sandbox.InverseSubbundleIndex;
import net.java.dev.weblets.sandbox.Subbundle;
import net.java.dev.weblets.util.StringUtils;

import java.util.*;

/**
 * Implementation of the weblets config
 * class
 * holds all the valid config and caching information
 * inside
 */
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

    /**
     * Adds a subbundle to our
     * parsed weblet xml!
     *
     * @param id        the subbundle id
     * @param resources the resources to be handled by the subbundle defintion
     */
    public void addSubbundle(String id, String resources) {
        String[] processedResources = resources.split("\\,");
        id = id.trim();
        Set subbundleFiles = new HashSet();
        Subbundle subbundle = _subbundles.findBundleFromId(id);
        if (subbundle == null) {
            subbundle = new Subbundle();
            subbundle.setSubbundleId(id);
        }
        //filter out the double bundles!
        subbundleFiles.addAll(subbundle.getResources());
        for (int cnt = 0; cnt < processedResources.length; cnt++) {
            String processedRespource = processedResources[cnt].trim();
            if (!subbundleFiles.contains(processedRespource)) {
                _subbundles.addBundle(processedRespource, subbundle);
                subbundleFiles.add(processedRespource);

            }
        }

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
        String allowedFiletypes = this.getInitParameter(Const.ALLOWED_RESOURCES);
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

    /**
     * gets a valid bundle from a given resource id
     *
     * @param resource the resource id to be used
     * @return a valid subbundle or null if none was found
     */
    public Subbundle getBundleFromResources(String resource) {
        return (Subbundle) _subbundles.findBundleFromResource(resource);
    }

    /**
     * fetches the subbundle from the given subbundle id
     *
     * @param bundleId the bundle id to be used
     * @return a valid subbundle or null if none was found!
     */
    public Subbundle getBundleFromId(String bundleId) {
        return _subbundles.findBundleFromId(bundleId);
    }

    /**
     * returns an entire collection of subbundles currently
     * registered in the config
     */
    public Collection getSubbundles() {
        return _subbundles.getSubbundles();
    }

    public Object getConfigParam(String key) {
        return _configParams.get(key);
    }

    public void setConfigParam(String key, Object value) {
        synchronized (_configParams) {
            _configParams.put(key, value);
        }
    }

    private Set _allowedResources = null;
    private WebletContainerImpl _container;
    private String _webletName;
    private String _webletClass;
    private String _webletVersion;
    private Map _initParams = new HashMap(3);
    private Map _mimeMappings = new HashMap(3);
    /*internal context params holder which stores additional processing values on a per weblet base!*/
    private Map _configParams = new HashMap(3);
    private InverseSubbundleIndex _subbundles = new InverseSubbundleIndex();
}
