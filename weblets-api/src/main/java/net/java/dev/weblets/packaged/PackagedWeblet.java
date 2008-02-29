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
package net.java.dev.weblets.packaged;

import java.io.IOException;
import java.net.URL;

import net.java.dev.weblets.Weblet;
import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.util.IStreamingFilter;
import net.java.dev.weblets.util.WebletTextprocessingFilter;
import net.java.dev.weblets.util.WebletsSimpleBinaryfilter;

/**
 * The standard packaged weblet 
 * serves resources from a standard weblet packaging
 * 
 */
public class PackagedWeblet extends Weblet {
	
	/**
	 * init method which is called by default
	 * to process the parameters
	 * @param config the webletconfig to be processed
	 */
	public void init(WebletConfig config) {
		super.init(config);
		//fetch the weblets init param
		String packageName = config.getInitParameter("package");
		//fetch the resource root param
		String resourceRoot = config.getInitParameter("resourceRoot");

		//init param missing, lets throw an error
		if (packageName == null && resourceRoot == null) {
			throw new WebletException(
					"Missing either init parameter \"package\" or "
							+ " or init parameter \"resourceRoot\" for "
							+ " Weblet \"" + config.getWebletName() + "\"");
		}
		//the init was successful we now have all we need
		_resourceRoot = (packageName != null) ? packageName.replace('.', '/')
				: resourceRoot;

	}

	public void service(WebletRequest request, WebletResponse response)
			throws IOException {
		String resourcePath = _resourceRoot + request.getPathInfo();

		//lets build up our filter chain which in our case is a binary filter for standard
		//processing and our text processing filter for text resources with included
		//weblet: functions
		IStreamingFilter filterChain = null;
		filterChain = new WebletsSimpleBinaryfilter();
		filterChain.addFilter(new WebletTextprocessingFilter());

		
		URL url = getResourceUrl(resourcePath);
		//our utils should handle the standard case
		WebletResourceloadingUtils.getInstance().loadFromUrl(getWebletConfig(),
				request, response, url, filterChain);
	}

	
	private URL getResourceUrl(String resourcePath) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(resourcePath);
		if (url == null) {
			loader = getClass().getClassLoader();
			url = loader.getResource(resourcePath);
		}
		return url;
	}

	public void destroy() {
		_resourceRoot = null;
		super.destroy();
	}

	private String _resourceRoot;

}
