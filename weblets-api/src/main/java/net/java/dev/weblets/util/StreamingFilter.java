package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import java.io.*;

/**
 *  Streaming filter base class
 *
 * @author: Werner Punz
 */
public abstract class StreamingFilter implements IStreamingFilter{
    Reader closeReader = null;
    Writer closeWriter = null;
    InputStream closeInputStream = null;
    OutputStream closeOutputStream = null;
    boolean isTriggered = false;
    StreamingFilter parentFilter = null;

    public abstract void addFilter(StreamingFilter filter);
    public abstract void filter(WebletConfig config, WebletRequest request,  WebletResponse response, InputStream in, OutputStream out) throws IOException;

    public void close() {
        if (parentFilter != null && !isTriggered) {
            parentFilter.close();
            return;
        }
        if (closeOutputStream != null)
            try {
                closeOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (closeReader != null)
            try {
                closeReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (closeWriter != null)
            try {
                closeWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (closeInputStream != null)
            try {
                closeInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
