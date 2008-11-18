package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.sandbox.SimpleCachingProvider;
import net.java.dev.weblets.sandbox.Cache;
import net.java.dev.weblets.packaged.ResourceloadingUtils;
import net.java.dev.weblets.resource.Subbundle;

import javax.print.DocFlavor;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Bundled Resource
 * <p/>
 * a bundled resource is
 * a pseudo resource only hosting a temp file
 * which concatenates all subresources!
 */
public class SubbundleResourceImpl extends BaseWebletResourceImpl {

    List /*WebletResource*/ _subresources = null;

    /**
     * helper entry to ease the invalidation
     */
    class CacheEntry {
        public byte[] data;
        long lastAccessed = -1;
    }

    /**
     * constructor
     *
     * @param config   our weblets config
     * @param request  the request showing either to the subbundle directly or to one of the files within the bundle!
     * @param resource the processed subbundle now having converted into a Subbundle resource
     */
    public SubbundleResourceImpl(WebletConfig config, WebletRequest request, ResourceResolver resolver, Subbundle resource) throws IOException {
        super(resource);
        _pathInfo = request.getPathInfo();
        _webletName = request.getWebletName();
        _subresources = new ArrayList(resource.getResources().size());
        Iterator it = resource.getResources().iterator();
        ProcessingWebletRequest shadowRequest = new ProcessingWebletRequest(request);
        ResourceFactory factory = ResourceloadingUtils.getInstance().getResourceFactory(config);
        while (it.hasNext()) {
            shadowRequest.setPathInfo((String) it.next());
            WebletResource subresource = factory.getResource(shadowRequest, resolver, false);/*subbundles always temped*/
            _subresources.add(subresource);
        }
        /*we set the mime type accordingly to the subbundle id*/
        setMimeType(config.getMimeType(resource.getSubbundleId()));
        super.setProcessTemp(true);
    }

    public InputStream getUnprocessedInputStream() {
        return null;  //we do not have one here
    }

    public InputStream getInputStream() throws IOException {
        // wo do a shadow copy our our stream resource if
        //the file is smaller than 10 KB
        Cache cache = SimpleCachingProvider.getInstance().getCache("resourceData");
        /*we cache the bundles to reduce disk hits if possible*/
        if (_tempFileSize == -1) {
            _tempFileSize = _temp.length();
        }
        if (_tempFileSize < 20000) {
            CacheEntry entry = (CacheEntry) cache.get(_webletName + ":" + _pathInfo);
            if (entry != null && entry.lastAccessed >= tempLastmodified()) {
                return new ByteArrayInputStream(entry.data);
            }
            entry = getCachedInputStream(cache);
            return new ByteArrayInputStream(entry.data);
        }
        return new FileInputStream(_temp);  //To change body of implemented methods use File | Settings | File Templates.
    }

    private CacheEntry getCachedInputStream(Cache cache) throws IOException {
        byte[] buffer;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream bis = new FileInputStream(_temp);
        copyStream(bis, baos);
        CacheEntry entry = new CacheEntry();
        entry.data = baos.toByteArray();
        entry.lastAccessed = _temp.lastModified();
        cache.put(_temp.getAbsolutePath(), entry);
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
        Iterator it = _subresources.iterator();
        long lastModified = -1;
        while (it.hasNext()) {
            WebletResource subResource = (WebletResource) it.next();
            lastModified = Math.min(lastModified, subResource.lastModified());
        }
        return lastModified;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List getSubresources() {
        return _subresources;
    }

    public void setSubresources(List subresources) {
        _subresources = subresources;
    }
}
