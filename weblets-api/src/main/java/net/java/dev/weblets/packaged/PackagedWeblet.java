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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.URL;
import java.net.URLConnection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.java.dev.weblets.Weblet;
import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletContainer;
import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

public class PackagedWeblet extends Weblet {

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
    }

    public void service(
            WebletRequest request,
            WebletResponse response) throws IOException {
        String resourcePath = _resourceRoot + request.getPathInfo();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(resourcePath);

        if (url != null) {
            URLConnection conn = url.openConnection();
            response.setLastModified(conn.getLastModified());
            response.setContentType(null); // Bogus "text/html" overriding mime-type
            response.setContentVersion(getWebletConfig().getWebletVersion());

            if (request.getIfModifiedSince() < conn.getLastModified()) {
                String contentType = response.getDefaultContentType();
                if (contentType.startsWith("text/") ||
                        contentType.endsWith("xml") ||
                        contentType.equals("application/x-javascript")) {
                    InputStream in = conn.getInputStream();
                    OutputStream out = response.getOutputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));

                    try {
                        while (reader.ready()) {
                            String line = reader.readLine();

                            // TODO: tidy up pattern matching, use leading single/double quote
                            // TODO: conditional filtering
                            int startWebletUrl = line.indexOf("weblet:url(");

                            if (startWebletUrl != -1) {
                                String protocol = line.substring(startWebletUrl);
                                Matcher matcher = _WEBLET_URL.matcher(protocol);
                                resolveLine(request, writer, line, startWebletUrl, matcher, true);
                            } else if((startWebletUrl = line.indexOf("weblet:resource(")) != -1) {
                                String protocol = line.substring(startWebletUrl);
                                Matcher matcher = _WEBLET_RESOURCE.matcher(protocol);
                                resolveLine(request, writer, line, startWebletUrl, matcher,false);
                            } else {
                                writer.write(line);
                                writer.println();
                            }
                        }
                    } finally {
                        reader.close();
                        writer.close();
                    }
                } else {
                    // only set Content-Length in advance for passthrough content.
                    response.setContentLength(conn.getContentLength());
                    InputStream in = conn.getInputStream();
                    OutputStream out = response.getOutputStream();

                    // binary passthrough
                    try {
                        byte[] buffer = new byte[1024];
                        
                        int len = 0;
                        while ((len = in.read(buffer)) > 0)
                            out.write(buffer, 0, len);

                    } finally {
                        in.close();
                        out.close();
                    }
                }
            } else {
                response.setStatus(WebletResponse.SC_NOT_MODIFIED);
            }
        } else {
            response.setStatus(WebletResponse.SC_NOT_FOUND);
        }
    }

    private void resolveLine(WebletRequest request, PrintWriter writer, String line, int startAt, Matcher matcher, boolean url) {
        if (matcher.matches()) {
            String preamble = line.substring(0, startAt);
            String webletName = matcher.group(1);
            webletName = (webletName != null) ? webletName.trim():webletName;

            String pathInfo = matcher.group(2);
            pathInfo = (pathInfo != null) ? pathInfo.trim():pathInfo;

            String postamble = matcher.group(3);

            // default relative weblet:/resource.ext to this weblet
            if (webletName == null) {
                webletName = getWebletConfig().getWebletName();
            }


            WebletContainer container = WebletContainer.getInstance();
            String webletURL = null;
            if(url)
                webletURL = container.getWebletContextPath()+container.getResourceUri(webletName, pathInfo);
            else
                webletURL = container.getResourceUri(webletName, pathInfo);

            writer.write(preamble);
            

            
            writer.write(webletURL);
            writer.write(postamble);
            writer.println();
        } else {
            writer.write(line);
            writer.println();
        }
    }

    public void destroy() {
        _resourceRoot = null;
        super.destroy();
    }
    private String _resourceRoot;

    //private static final Pattern _WEBLET_URL =
    //                        Pattern.compile("weblet:(?://([^\"\\'/]+))?(/[^/]{1}[^\"\\']*)?(.*)");
    private static final Pattern _WEBLET_URL =
            Pattern.compile("weblet:\\s*url\\s*\\(([^\"\\'/]+)\\,([^\"\\'\\)]+)\\)?(.*)");
    private static final Pattern _WEBLET_RESOURCE =
            Pattern.compile("weblet:\\s*resource\\s*\\(([^\"\\'/]+)\\,([^\"\\'\\)]+)\\)?(.*)");

    public static void main(String[] argv) {
        Matcher matcher = _WEBLET_URL.matcher("weblet:url(demo,/helloworld.js)postampletext");
        if (matcher.matches()) {
            String webletName = matcher.group(1);
            System.out.println(webletName);
            String pathInfo = matcher.group(2);
            System.out.println(pathInfo);
            String postamble = matcher.group(3);
            System.out.println(postamble);
        }
    }
}
