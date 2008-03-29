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
    void copy(WebletRequest request, WebletResponse response, InputStream in, OutputStream out) throws IOException;
       
}
