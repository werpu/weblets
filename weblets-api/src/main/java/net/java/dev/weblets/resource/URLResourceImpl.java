package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.resource.Cache;
import net.java.dev.weblets.resource.SimpleCachingProvider;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author werpu
 * @date: 11.11.2008
 * <p/>
 * <p/>
 * An implementation of our url resource loading mechanism
 * the entire caching aspects are handled here as well!
 */
public class URLResourceImpl extends BaseWebletResourceImpl {

    long _lastModified = 0;
    private static final int CACHE_FILESIZE = 20000;

    /**
     * helper entry to ease the invalidation
     */
    class CacheEntry {
        public byte[] data;
        long lastAccessed = -1;
    }

    public URLResourceImpl(WebletConfig config, WebletRequest request, URL resource) throws IOException {
        super(resource);
        URLConnection conn = null;
        _webletName = request.getWebletName();
        _pathInfo = request.getPathInfo();
        conn = resource.openConnection();
        _lastModified = conn.getLastModified();
        super.setMimeType(config.getMimeType(request.getPathInfo()));
        /*set the mime type*/
    }

    public URLResourceImpl(WebletConfig config, String webletName, String pathInfo, String mimetype, URL resource) throws IOException {
        super(resource);
        URLConnection conn = null;
        conn = resource.openConnection();
        _webletName = webletName;
        _pathInfo = pathInfo;
        _lastModified = conn.getLastModified();
        super.setMimeType(mimetype);
        /*set the mime type*/
    }

    public InputStream getUnprocessedInputStream() throws IOException {
        URLConnection conn = ((URL) _resource).openConnection();
        return conn.getInputStream();
    }

    public InputStream getInputStream() throws IOException {
        Cache cache = SimpleCachingProvider.getInstance().getCache("resourceData");
        if (_temp != null) {
            return handleCachedTempFile(cache);
        }
        if (_tempFileSize == -1) { //no temp file size given for now
            URLConnection conn = ((URL) _resource).openConnection();
            _tempFileSize = conn.getContentLength();
        }
        if (_tempFileSize < CACHE_FILESIZE) {
            return handleCachedConnection(cache);
        }
        return handleUncachedConnection();
    }

    private InputStream handleUncachedConnection() throws IOException {
        return ((URL) _resource).openConnection().getInputStream();
    }

    private InputStream handleCachedConnection(Cache cache) throws IOException {
        CacheEntry entry = (CacheEntry) cache.get(_webletName + ":" + _pathInfo);
        if (entry != null && entry.lastAccessed >= lastModified()) {
            return new ByteArrayInputStream(entry.data);
        }
        URLConnection conn = ((URL) _resource).openConnection();
        byte[] buffer = getCachedInputStream(cache, conn.getInputStream()).data;
        return new ByteArrayInputStream(buffer);
    }

    private InputStream handleCachedTempFile(Cache cache) throws IOException {
        // wo do a shadow copy our our stream resource if
        //the file is smaller than 10 KB
        /*we cache the bundles to reduce disk hits if possible*/
        if (_tempFileSize == -1) {
            _tempFileSize = _temp.length();
            getCachedInputStream(cache, new FileInputStream(_temp));
        }
        if (_tempFileSize < CACHE_FILESIZE) {
            CacheEntry entry = (CacheEntry) cache.get(_webletName + ":" + _pathInfo);
            if (entry != null && entry.lastAccessed >= lastModified()) {
                return new ByteArrayInputStream(entry.data);
            }
            entry = getCachedInputStream(cache, new FileInputStream(_temp));
            return new ByteArrayInputStream(entry.data);
        }
        return new FileInputStream(_temp);
    }

    private CacheEntry getCachedInputStream(Cache cache, InputStream istr) throws IOException {
        byte[] buffer;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream bis = istr;
        copyStream(bis, baos);
        buffer = baos.toByteArray();
        CacheEntry entry = new CacheEntry();
        entry.data = buffer;
        entry.lastAccessed = _lastModified;
        cache.put(_webletName + ":" + _pathInfo, entry);
        return entry;
    }

    protected void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        BufferedInputStream bufIn = new BufferedInputStream(in);
        BufferedOutputStream bufOut = new BufferedOutputStream(out);
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

    public long lastModified() {
        if (_temp != null) {
            return _temp.lastModified();
        }
        try {
            URLConnection conn = ((URL) _resource).openConnection();
            return conn.getLastModified();
        } catch (IOException ex) {
            return -1;
        }
    }
}
