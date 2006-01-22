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

import java.io.IOException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

abstract public class WebletContainer
{
  static protected void setInstance(
    WebletContainer container) throws WebletException
  {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    _INSTANCES.put(loader, container);
  }

  static public WebletContainer getInstance() throws WebletException
  {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    return (WebletContainer)_INSTANCES.get(loader);
  }

  abstract public void service(
    WebletRequest  request,
    WebletResponse response) throws IOException, WebletException;

  abstract public String getWebletURL(
    String webletName,
    String pathInfo) throws WebletException;

  static private final Map _INSTANCES =
                              Collections.synchronizedMap(new HashMap());
}
