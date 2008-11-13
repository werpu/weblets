package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author werpu
 * @date: 11.11.2008
 */
public class PathResourceImpl extends BaseWebletResourceImpl {

    public PathResourceImpl(WebletConfig config, WebletRequest request, String resource) throws IOException {
        super(resource);
        File resourceHandle = new File(resource);
        _resource = resourceHandle;
        super.setMimeType(config.getMimeType(resource));
        /*set the mime type*/
    }

    public InputStream getUnprocessedInputStream() throws IOException {
        return new FileInputStream((File) _resource);
    }

    public InputStream getInputStream() throws IOException {
        if (_tempResource != null) {
            return new FileInputStream(_tempResource);
        }
        return new FileInputStream((File) _resource);
    }

    public long lastModified() {
        return ((File) _resource).lastModified();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
