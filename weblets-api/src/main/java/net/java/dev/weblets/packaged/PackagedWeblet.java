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

import net.java.dev.weblets.*;
import net.java.dev.weblets.util.IStreamingFilter;
import net.java.dev.weblets.util.WebletsSimpleBinaryfilter;
import net.java.dev.weblets.util.WebletTextprocessingFilter;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class PackagedWeblet extends Weblet {
    IStreamingFilter filterChain = null;
    public void init(
            WebletConfig config) {
        super.init(config);
        String packageName = config.getInitParameter("package");
        String resourceRoot = config.getInitParameter("resourceRoot");

        if (packageName == null && resourceRoot == null) {
            throw new WebletException("Missing either init parameter \"package\" or " +
                    " or init parameter \"resourceRoot\" for " +
                    " Weblet \"" + config.getWebletName() + "\"");
        }
        _resourceRoot = (packageName != null) ? packageName.replace('.', '/')
                : resourceRoot;
        filterChain = new WebletsSimpleBinaryfilter();
        filterChain.addFilter(new WebletTextprocessingFilter());
        
    }

    public void service(
            WebletRequest request,
            WebletResponse response) throws IOException {
        String resourcePath = _resourceRoot + request.getPathInfo();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(resourcePath);

        WebletResourceloadingUtils.getInstance().loadFromUrl(getWebletConfig(), request, response, url, filterChain);
    }


    public void destroy() {
        _resourceRoot = null;
        super.destroy();
    }

    private String _resourceRoot;


}
