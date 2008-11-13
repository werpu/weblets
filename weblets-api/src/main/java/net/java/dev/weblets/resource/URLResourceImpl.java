package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author werpu
 * @date: 11.11.2008
 */
public class URLResourceImpl extends BaseWebletResourceImpl {

    long _lastModified = 0;

    public URLResourceImpl(WebletConfig config, WebletRequest request, URL resource) throws IOException {
        super(resource);
        URLConnection conn = null;
        conn = resource.openConnection();
        _lastModified = conn.getLastModified();
        super.setMimeType(config.getMimeType(request.getPathInfo()));
        /*set the mime type*/
    }

    public URLResourceImpl(WebletConfig config, String mimetype, URL resource) throws IOException {
        super(resource);
        URLConnection conn = null;
        conn = resource.openConnection();
        _lastModified = conn.getLastModified();
        super.setMimeType(mimetype);
        /*set the mime type*/
    }

    public InputStream getUnprocessedInputStream() throws IOException {
        URLConnection conn = ((URL) _resource).openConnection();
        return conn.getInputStream();
    }

    public InputStream getInputStream() throws IOException {
        if (_tempResource != null) {
            return new FileInputStream(_tempResource);
        }
        URLConnection conn = ((URL) _resource).openConnection();
        return conn.getInputStream();
    }

    public long lastModified() {
        if (_tempResource != null) {
            return _tempResource.lastModified();
        }
        try {
            URLConnection conn = ((URL) _resource).openConnection();
            return conn.getLastModified();
        } catch (IOException ex) {
            return -1;
        }
    }
}
