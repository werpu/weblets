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
	public StreamingFilter getParentFilter() {
		return parentFilter;
	}
	public void setParentFilter(StreamingFilter parentFilter) {
		this.parentFilter = parentFilter;
	}
	public boolean isTriggered() {
		return isTriggered;
	}
	public void setTriggered(boolean isTriggered) {
		this.isTriggered = isTriggered;
	}
	public Reader getCloseReader() {
		return closeReader;
	}
	public void setCloseReader(Reader closeReader) {
		this.closeReader = closeReader;
	}
	public Writer getCloseWriter() {
		return closeWriter;
	}
	public void setCloseWriter(Writer closeWriter) {
		this.closeWriter = closeWriter;
	}
	public InputStream getCloseInputStream() {
		return closeInputStream;
	}
	public void setCloseInputStream(InputStream closeInputStream) {
		this.closeInputStream = closeInputStream;
	}
	public OutputStream getCloseOutputStream() {
		return closeOutputStream;
	}
	public void setCloseOutputStream(OutputStream closeOutputStream) {
		this.closeOutputStream = closeOutputStream;
	}
}
