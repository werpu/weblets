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

import javax.servlet.ServletRequest;

import net.java.dev.weblets.impl.WebletRequestBase;
import net.java.dev.weblets.impl.misc.ReflectUtils;

public class WebletRequestImpl extends WebletRequestBase {
	public WebletRequestImpl(String webletName, String webletPath, String contextPath, String pathInfo, long ifModifiedSince, Object externalRequest) {
		super(webletName, webletPath, contextPath, pathInfo, ifModifiedSince, externalRequest);
	}

	public String getParameter(String name) {
		return (String) ReflectUtils.getParameter(getExternalRequest(), name);
	}
}
