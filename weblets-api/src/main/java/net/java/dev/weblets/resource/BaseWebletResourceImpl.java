package net.java.dev.weblets.resource;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

/**
 * @author werpu
 * @date: 11.11.2008
 * <p/>
 * Base class for a resource implementation
 * to encapsulate the resource
 * handle as neutral datasource
 * instead of URLs as before!
 */
public abstract class BaseWebletResourceImpl implements WebletResource {

    protected Object _resource;

    protected File _temp; /*temp file handle for processed files being put into the temp dir*/

    protected byte[] _inMemoryTemp; /*in memory temp cache to prevent server hammering we offload small resources into the mem*/

    protected String _resourcePath;

    String _mimeType;

    /**
     * if set to true the engine has to do a temp file processing
     * otherwise the processing happens directly on _resource
     */
    boolean _processTemp = false;

    public boolean isRecreateTemp() {
        return _recreateTemp;
    }

    public void setRecreateTemp(boolean recreateTemp) {
        _recreateTemp = recreateTemp;
    }

    boolean _recreateTemp = true;

    protected BaseWebletResourceImpl(Object resource) {
        _resource = resource;
    }

    public void setResourcePath(String resourcePath) {
        _resourcePath = resourcePath;
    }

    public void setMimeType(String mimeType) {
        _mimeType = mimeType;
    }

    /**
     * @return the unprocessed resource handle
     */
    public Object getUnoprocessedResourceHandle() {
        return _resource;
    }

    public abstract InputStream getUnprocessedInputStream() throws IOException;

    /**
     * @return Returns an input string on the processed resource
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * @return a relative path to the resource
     *         sort of the resource identifier
     */
    public String getResourcePath() {
        return _resourcePath;
    }

    /**
     * @return the processed lastmodified date
     *         for the resource
     */
    public abstract long lastModified();

    /**
     * @return the mime type for the resource
     */
    public String getMimetype() {
        return _mimeType;
    }

    /**
     * @return the resource version
     *         not implemented yet
     */
    public String getResourceVersion() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public boolean hasTemp() {
        return _temp != null;
    }

    public long tempLastmodified() {
        if (_temp == null) {
            return -1;
        }
        return _temp.lastModified();
    }

    public File getTemp() {
        return _temp;
    }

    public void setTemp(File temp) {
        _temp = temp;
    }

    public boolean isProcessTemp() {
        return _processTemp;
    }

    public void setProcessTemp(boolean processTemp) {
        _processTemp = processTemp;
    }
}
