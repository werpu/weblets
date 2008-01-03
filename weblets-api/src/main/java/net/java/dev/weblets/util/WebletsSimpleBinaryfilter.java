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
public class WebletsSimpleBinaryfilter extends StreamingFilter  {

    private boolean isTriggered(WebletRequest request, WebletResponse response) {
        String contentType = response.getDefaultContentType();
        super.isTriggered = !(contentType.startsWith("text/") ||
                contentType.endsWith("xml") ||
                contentType.equals("application/x-javascript"));
        return super.isTriggered;
    }

    public void addFilter(StreamingFilter filter) {
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

        if (parentFilter != null) {
            closeReader = reader = new InputStreamReader(new PipedInputStream(pipeOut));
            closeInputStream = in; /*we have a break in the input oput chain due to the pipe*/
            FilterThread filterThread = new FilterThread(parentFilter, config, request, response, in, pipeOut);
            filterThread.start();
        } else {
            closeReader = reader = new InputStreamReader(in);
        }

        byte[] buffer = new byte[2048];

        int len = 0;
        int total = 0;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            total += len;
        }
        closeOutputStream = out;
        synchronized (response) {
            response.setContentLength(total);
        }
    }
}
