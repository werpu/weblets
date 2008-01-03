package net.java.dev.weblets.packaged;

import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletContainer;
import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletResponse;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;
import java.net.URLConnection;

/**
 * helper class to be shared by various weblet loaders
 * 
 */
public class WebletResourceloadingUtils {
    static WebletResourceloadingUtils instance = new WebletResourceloadingUtils();

    public static  WebletResourceloadingUtils getInstance() {
        return instance;
    }


    public void loadFromUrl(WebletConfig config, WebletRequest request, WebletResponse response, URL url) throws IOException {
           if (url != null) {
               URLConnection conn = url.openConnection();
               response.setLastModified(conn.getLastModified());
               response.setContentType(null); // Bogus "text/html" overriding mime-type
               response.setContentVersion(config.getWebletVersion());

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
                           handleTextResource(config, request, reader, writer);
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


    public void handleTextResource(WebletConfig config, WebletRequest request, BufferedReader reader, PrintWriter writer) throws IOException {
        while (reader.ready()) {
            String line = reader.readLine();

            // TODO: tidy up pattern matching, use leading single/double quote
            // TODO: conditional filtering
            int startWebletUrl = line.indexOf("weblet:url(");

            if (startWebletUrl != -1) {
                String protocol = line.substring(startWebletUrl);
                Matcher matcher = _WEBLET_URL.matcher(protocol);
                resolveLine(config, request, writer, line, startWebletUrl, matcher, true);
            } else if((startWebletUrl = line.indexOf("weblet:resource(")) != -1) {
                String protocol = line.substring(startWebletUrl);
                Matcher matcher = _WEBLET_RESOURCE.matcher(protocol);
                resolveLine(config, request, writer, line, startWebletUrl, matcher,false);
            } else {
                writer.write(line);
                writer.println();
            }
        }
    }

    private void resolveLine(WebletConfig config, WebletRequest request, PrintWriter writer, String line, int startAt, Matcher matcher, boolean url) {
        if (matcher.matches()) {
            String preamble = line.substring(0, startAt);
            String webletName = matcher.group(1);
            webletName = (webletName != null) ? webletName.trim():webletName;

            String pathInfo = matcher.group(2);
            pathInfo = (pathInfo != null) ? pathInfo.trim():pathInfo;

            String postamble = matcher.group(3);

            // default relative weblet:/resource.ext to this weblet
            if (webletName == null) {
                webletName = config.getWebletName();
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
    
    public static final Pattern _WEBLET_URL =
            Pattern.compile("weblet:\\s*url\\s*\\(([^\"\\'/]+)\\,([^\"\\'\\)]+)\\)?(.*)");
    public static final Pattern _WEBLET_RESOURCE =
            Pattern.compile("weblet:\\s*resource\\s*\\(([^\"\\'/]+)\\,([^\"\\'\\)]+)\\)?(.*)");

}
