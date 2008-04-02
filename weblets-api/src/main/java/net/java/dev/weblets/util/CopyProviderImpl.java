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


    public void copy(WebletRequest request, String contentType, InputStream in, OutputStream out) throws IOException {

        boolean isText = isText(contentType);

        if (isText)
            copyText(request,  new InputStreamReader(in), new OutputStreamWriter(out));
        else
            copyStream(request,  in, out);

    }

    private boolean isText(String contentType) {
        return contentType != null && (contentType.startsWith("text/") ||
                contentType.endsWith("xml") ||
                contentType.equals("application/x-javascript"));
    }

    /**
     * wraps the input stream from our given request into another input stream
     *
     * @param request the request which serves as the streaming source
     * @param mimetype the response mimetype
     * @param in our given input steam
     * @return
     * @throws IOException
     */
    public InputStream wrapInputStream(WebletRequest request, String mimetype, InputStream in) throws IOException {
        boolean isText = isText(mimetype);
        if(isText) {
            BufferedReader bufIn = new BufferedReader(mapResponseReader(request,new InputStreamReader(in)));
            return new ReaderInputStream(bufIn);
        }
        return new BufferedInputStream(mapInputStream(request, in));
    }

    protected BufferedWriter mapResponseWriter(WebletRequest request, Writer out) {
        return new BufferedWriter(out);
    }

    protected  BufferedReader mapResponseReader(WebletRequest request, Reader in) {
        return new TextProcessingReader(in, request.getWebletName());
    }


    protected  BufferedInputStream mapInputStream(WebletRequest request, InputStream in) {
        return new BufferedInputStream(in);
    }

    protected  BufferedOutputStream mapOutputStream(WebletRequest request, OutputStream out) {
        return new BufferedOutputStream(out);
    }


    protected void copyText(WebletRequest request, Reader in, Writer out) throws IOException {
        byte[] buffer = new byte[2048];

        int len = 0;
        int total = 0;
        BufferedReader bufIn = mapResponseReader(request, in);
        BufferedWriter bufOut = mapResponseWriter(request, out);
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


    protected void copyStream(WebletRequest request, InputStream in, OutputStream out) throws IOException {

        byte[] buffer = new byte[2048];

        BufferedInputStream bufIn = mapInputStream(request, in);
        BufferedOutputStream bufOut = mapOutputStream(request, out);

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
