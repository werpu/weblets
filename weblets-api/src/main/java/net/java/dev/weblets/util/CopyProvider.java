package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: werpu
 * Date: 20.03.2008
 * Time: 19:19:38
 * To change this template use File | Settings | File Templates.
 */
public interface CopyProvider {
    void copy(WebletRequest request, WebletResponse response, InputStream in, OutputStream out) throws IOException;
       
}
