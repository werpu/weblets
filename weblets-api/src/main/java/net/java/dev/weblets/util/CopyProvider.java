package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Central contractual interface for
 * our copy provider control
 * usually a copy provider
 * chains some input output streams together
 * to get a certain behavior
 * 
 */
public interface CopyProvider {

    /**
     * central copy method
     * @param request the request
     * @param mimetype the response mimetype
     * @param in the incoming data input stream
     * @param out the receiving steam
     * @throws IOException  in case of an error
     */
    void copy(WebletRequest request, String mimetype, InputStream in, OutputStream out) throws IOException;

    /**
     * wraps the incoming input stream with out post processing
     * filters
     * @param request the incoming request
     * @param in the incoming stream
     *
     * @return an inputstream with the resource or null if none is found
     * @throws IOException in case of a severe error
     */
    public InputStream wrapInputStream(WebletRequest request, String mimetype, InputStream in) throws IOException;
}