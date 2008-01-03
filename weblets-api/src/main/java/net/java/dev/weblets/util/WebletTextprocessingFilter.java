package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletContainer;
import net.java.dev.weblets.WebletResponse;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * TODO: DESC
 *
 * @author: Werner Punz
 * @date: 03.01.2008.
 */
public class WebletTextprocessingFilter implements IStreamingFilter{

    IStreamingFilter parentFilter = null;

    public static final Pattern _WEBLET_URL =
            Pattern.compile("weblet:\\s*url\\s*\\(([^\"\\'/]+)\\,([^\"\\'\\)]+)\\)?(.*)");
    public static final Pattern _WEBLET_RESOURCE =
            Pattern.compile("weblet:\\s*resource\\s*\\(([^\"\\'/]+)\\,([^\"\\'\\)]+)\\)?(.*)");

    public void addFilter(IStreamingFilter filter) {
        parentFilter = filter;
    }

    private boolean isTriggered(WebletRequest request, WebletResponse response) {
        String contentType =  response.getDefaultContentType();
        return contentType.startsWith("text/") ||
                           contentType.endsWith("xml") ||
                           contentType.equals("application/x-javascript");
    }

    public void filter(WebletConfig config, WebletRequest request, WebletResponse response, InputStream in, OutputStream out) throws IOException {
        if(!isTriggered(request, response)) {
            if(parentFilter != null)
                parentFilter.filter(config, request, response, in, out);
            return;
        }
        
        BufferedReader reader = null;
        if(parentFilter != null) {
            PipedOutputStream pipeOut = new PipedOutputStream();
            parentFilter.filter(config, request, response, in, pipeOut);
            reader = new BufferedReader(new InputStreamReader(new PipedInputStream(pipeOut)));
        } else {
             reader = new BufferedReader(new InputStreamReader(in));
        }
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(out));
        while (reader.ready()) {
            String line = reader.readLine();

            // TODO: tidy up pattern matching, use leading single/double quote
            // TODO: conditional filtering
            int startWebletUrl = line.indexOf("weblet:url(");

            if (startWebletUrl != -1) {
                String protocol = line.substring(startWebletUrl);
                Matcher matcher = _WEBLET_URL.matcher(protocol);
                resolveLine(config,  writer, line, startWebletUrl, matcher, true);
            } else if((startWebletUrl = line.indexOf("weblet:resource(")) != -1) {
                String protocol = line.substring(startWebletUrl);
                Matcher matcher = _WEBLET_RESOURCE.matcher(protocol);
                resolveLine(config,  writer, line, startWebletUrl, matcher,false);
            } else {
                writer.write(line);
                writer.println();
            }

        }
        writer.flush();
    }


    private void resolveLine(WebletConfig config,  PrintWriter writer, String line, int startAt, Matcher matcher, boolean url) {


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


}
