package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import java.io.*;

/**
 * A simple binary filter serving a binary resource without any further processing
 *
 * <p>
 * note: this class is not thread save, it always should be used in a single thread
 * only!
 * The same goes for the reference chains
 * the threading of the referenced chain elements is enforced by the java
 * pipe api and does not disrupt this limitation!
 * </p>
 *
 * Do not chain yet with more than one filter triggering
 * this feature has not been tested yet with more than one filter
 * active at one time, but theoretically in the long
 * run the parallel filter processing should give a speedup
 * of 20-30% compared to normal pipes
 * (similar to execution pips in micro processors, but here
 * on a way bigger level)
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
            closeInputStream = in; /*we have a break in the input output chain due to the pipe*/
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
