package net.java.dev.weblets.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

/**
 * Internal Resource handle to deal with
 * temped and not temp files!
 * <p/>
 * We have moved from URLs to this more neutral format
 * <p/>
 */
public interface WebletResource {

    /**
     * @return the unprocessed resource handle
     */
    public Object getUnoprocessedResourceHandle() throws IOException;

    /**
     * @return Returns an input string on the processed resource
     */
    public InputStream getInputStream() throws IOException;

    /**
     * @return a relative path to the resource
     *         sort of the resource identifier
     */
    public String getResourcePath();

    /**
     * @return the processed lastmodified date
     *         for the resource
     */
    public long lastModified();

    /**
     * @return the mime type for the resource
     */
    public String getMimetype();

    /**
     * @return the resource version
     *         not implemented yet
     */
    public String getResourceVersion();

    /**
     * @return true if the resource has a temporary processed copy lingering
     */
    public boolean hasTemp();

    /**
     * @return the last modified date of the temp shadow copy
     */
    public long tempLastmodified();

    /**
     * @return getter for the temp shadow copy file handle
     */
    public File getTemp();

    /**
     * setter for the temp shadow copy file handle!
     *
     * @param temp
     */
    public void setTemp(File temp);

    /**
     * @return if set to true, the stored temp file must be recreated on a recreate condition!
     */
    public boolean isRecreateTemp();

    /**
     * @param recreateTemp trigger to issue a temp recreation
     */
    public void setRecreateTemp(boolean recreateTemp);

    /**
     * means resource allows temp processing
     *
     * @return
     */
    public boolean isProcessTemp();

    /**
     * means resource does not allow temp processing
     *
     * @param processTemp
     */
    public void setProcessTemp(boolean processTemp);

    /**
     * @return a stream object on the unprocessed resource
     */
    public InputStream getUnprocessedInputStream() throws IOException;
}
