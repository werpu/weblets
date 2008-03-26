package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import java.io.*;


/**
 * A provider which does
 * the central copy operation
 * the valve knows its filtering
 * pipes and is returned by
 * the central processing
 * factory
 */
public class CopyProviderImpl implements CopyProvider {


    public void copy(WebletRequest request, WebletResponse response, InputStream in, OutputStream out) throws IOException {

        String contentType = response.getDefaultContentType();
        boolean isText = contentType != null && (contentType.startsWith("text/") ||
                contentType.endsWith("xml") ||
                contentType.equals("application/x-javascript"));

        if (isText)
            copyText(request, response, new InputStreamReader(in), new OutputStreamWriter(out));
        else
            copyStream(request, response, in, out);

    }

    public Writer mapResponseWriter(WebletRequest request, WebletResponse response, Writer out) {
        return new TextProcessingWriter(out, request.getWebletName());
    }

    public Reader mapResponseReader(WebletRequest request, WebletResponse response, Reader in) {
        return in;
    }


    public InputStream mapInputStream(WebletRequest request, WebletResponse response, InputStream in) {
        return in;
    }

    public OutputStream mapOutputStream(WebletRequest request, WebletResponse response, OutputStream out) {
        return out;
    }


    protected void copyText(WebletRequest request, WebletResponse response, Reader in, Writer out) throws IOException {
        byte[] buffer = new byte[2048];

        int len = 0;
        int total = 0;
        BufferedReader bufIn = new BufferedReader(mapResponseReader(request, response, in));
        BufferedWriter bufOut = new BufferedWriter(mapResponseWriter(request, response, out));
        try {
            String line = null;
            while ((line = bufIn.readLine()) != null) {
                bufOut.write(line);
                bufOut.write("\n");
            }
        } finally {
            bufIn.close();
            bufOut.close();
        }

    }


    protected void copyStream(WebletRequest request, WebletResponse response, InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];

        BufferedInputStream bufIn = new BufferedInputStream(mapInputStream(request, response, in));
        BufferedOutputStream bufOut = new BufferedOutputStream(mapOutputStream(request, response, out));

        int len = 0;
        int total = 0;
        try {
            while ((len = bufIn.read(buffer)) > 0) {
                bufOut.write(buffer, 0, len);
                total += len;
            }
        } finally {
            bufIn.close();
            bufOut.close();
        }
    }

}
