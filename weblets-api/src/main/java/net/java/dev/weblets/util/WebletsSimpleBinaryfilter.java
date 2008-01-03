package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import java.io.*;

/**
 * TODO: DESC
 *
 * @author: Werner Punz
 * @date: 03.01.2008.
 */
public class WebletsSimpleBinaryfilter implements IStreamingFilter {
    IStreamingFilter parentFilter = null;

    private boolean isTriggered(WebletRequest request, WebletResponse response) {
        String contentType = response.getDefaultContentType();
        return !(contentType.startsWith("text/") ||
                contentType.endsWith("xml") ||
                contentType.equals("application/x-javascript"));
    }

    public void addFilter(IStreamingFilter filter) {
        parentFilter = filter;
    }

    public void filter(WebletConfig config, WebletRequest request, WebletResponse response, InputStream in, OutputStream out) throws IOException {
        if (!isTriggered(request, response)) {
            if (parentFilter != null)
                parentFilter.filter(config, request, response, in, out);
            return;
        }

        PipedOutputStream pipeOut = new PipedOutputStream();
        InputStreamReader reader = null;

         if(parentFilter != null) {
            parentFilter.filter(config, request, response, in, pipeOut);
            reader = new InputStreamReader(new PipedInputStream(pipeOut));
        } else {
             reader = new InputStreamReader(in);
        }

        byte[] buffer = new byte[2048];

        int len = 0;
        int total = 0;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            total += len;
        }
        response.setContentLength(total);
    }
}
