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
import java.io.InputStream;

/**
 * Basic weblet class all resource loading handlers must extend this class
 * 
 */
abstract public class Weblet {
	public int getWebletType() {
		return WebletConfig.WEBLET_TYPE_LOCAL;
	}

	public void init(WebletConfig config) {
		_config = config;
	}

	public void destroy() {
		_config = null;
	}

	public WebletConfig getWebletConfig() {
		return _config;
	}

	abstract public void service(WebletRequest request, WebletResponse response) throws IOException, WebletException;

	/**
	 * Second Weblet entry point the service stream method is used internally for Weblets 1.1 by our asynchronous reporting interface
	 * 
	 * It basically does the same as service but must be servlet independend (aka it cannot rely on a base servlet or the external request of the weblet request
	 * object
	 * 
	 * If you do not trigger the reporting subengine then you can omit this interface it is not used internally
	 * 
	 * Note, we limit our params here to pathInfo and mimetype since we cannot rely on having a valid external context available weblets themselves know their
	 * config params hence all config params can be reached via their internal settings, but in no circumstance you can rely on the weblets having any context
	 * whatsoever in this case!
	 * 
	 * 
	 * @param pathInfo
	 *            the pathinfo for the local resource
	 * 
	 * @param mimetype
	 *            the preferred mimetype for the request
	 * @return
	 */
	abstract public InputStream serviceStream(String pathInfo, String mimetype) throws IOException, WebletException;

	private WebletConfig	_config;
}