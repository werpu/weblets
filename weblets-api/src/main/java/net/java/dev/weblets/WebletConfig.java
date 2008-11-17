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
package net.java.dev.weblets;

import net.java.dev.weblets.sandbox.Subbundle;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;

public interface WebletConfig {
    static int WEBLET_TYPE_LOCAL = 1;
    static int WEBLET_TYPE_PROXY = 2;
    static int WEBLET_TYPE_REDIRECT = 3;
    static int WEBLET_TYPE_OTHER = 100;

    /**
     * @return the container hosting the resources
     */
    public WebletContainer getWebletContainer();

    /**
     * @return the name of the current weblet
     */
    public String getWebletName();

    /**
     * @return the version of the current weblet
     */
    public String getWebletVersion();

    /**
     * fetches the init param upon its given name
     *
     * @param paramName
     * @return
     */
    public String getInitParameter(String paramName);

    /**
     * gets the list of init parameters for a
     * given weblet
     *
     * @return
     */
    public Iterator getInitParameterNames();

    /**
     * Returns the mimetype of a given resource path
     *
     * @param resourcePath
     * @return
     */
    public String getMimeType(String resourcePath);

    /**
     * gets a valid bundle from a given resource id
     *
     * @param resurcePath the resource id to be used
     * @return a valid subbundle or null if none was found
     */
    public Subbundle getBundleFromResources(String resurcePath);

    /**
     * fetches the subbundle from the given subbundle id
     *
     * @param bundleId the bundle id to be used
     * @return a valid subbundle or null if none was found!
     */
    public Subbundle getBundleFromId(String bundleId);

    /**
     * returns an entire collection of subbundles currently
     * registered in the config
     */
    public Collection getSubbundles();

    /**
     * @return a list of allowed resources depending on the init params
     *         of the weblet
     */
    public Set getAllowedResources();

    /**
     * Holder for temporary configuration
     * parameters which can be set outside
     *
     * @param key the key for the configuration parameter
     * @return an object which is the value for the config param
     */
    public Object getConfigParam(String key);

    /**
     * setter for the configuration params
     *
     * @param key   the key
     * @param value the value!
     */
    public void setConfigParam(String key, Object value);

    /**
     * returns true if the config has subbundles
     *
     * @return
     */
    public boolean hasSubbundles();

    /**
     * mapping function to map a resourcePath into its subbundle resource path!
     *
     * @param resourcePath to be mapped
     * @return
     */
    public String getSubbundleIdFromResource(String resourcePath);
}