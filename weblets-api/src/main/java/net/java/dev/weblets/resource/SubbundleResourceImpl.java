package net.java.dev.weblets.resource;

import net.java.dev.weblets.sandbox.Subbundle;
import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.packaged.WebletResourceloadingUtils;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;

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
     * constructor
     *
     * @param config   our weblets config
     * @param request  the request showing either to the subbundle directly or to one of the files within the bundle!
     * @param resource the processed subbundle now having converted into a Subbundle resource
     */
    public SubbundleResourceImpl(WebletConfig config, WebletRequest request, ResourceResolver resolver, Subbundle resource) throws IOException {
        super(resource);
        String oldPathInfo = request.getPathInfo();
        _subresources = new ArrayList(resource.getResources().size());
        Iterator it = resource.getResources().iterator();
        ProcessingWebletRequest shadowRequest = new ProcessingWebletRequest(request);
        ResourceFactory factory = WebletResourceloadingUtils.getInstance().getResourceFactory(config);
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

        return new FileInputStream(_temp);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public long lastModified() {
        Iterator it = _subresources.iterator();
        long lastModified = System.currentTimeMillis();
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
