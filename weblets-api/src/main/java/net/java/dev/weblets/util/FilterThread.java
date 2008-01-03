package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * TODO: DESC
 *
 * @author: Werner Punz
 * @date: 03.01.2008.
 */
class FilterThread extends Thread {
    public boolean isFinished() {
        return finished;
    }

    boolean finished = false;
    WebletConfig config;
    WebletRequest request;
    WebletResponse response;
    InputStream in;
    OutputStream out;
    IStreamingFilter filter;

    public FilterThread(IStreamingFilter filter, WebletConfig config, WebletRequest request, WebletResponse response, InputStream in, OutputStream out) {
        this.config = config;
        this.request = request;
        this.response = response;
        this.in = in;
        this.out = out;
        this.filter = filter;
    }

    public void run() {
        try {
            filter.filter(config, request, response, in, out);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            finished = true;
        }
    }

}
