package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletRequest;

/**
 * an internal request delegate to allow additional processing
 */
class ProcessingWebletRequest implements WebletRequest {

    WebletRequest _delegate;

    String _shadowPathInfo = null;

    public ProcessingWebletRequest(WebletRequest delegate) {
        _delegate = delegate;
    }

    public String getWebletName() {
        return _delegate.getWebletName();
    }

    public String getWebletPath() {
        return _delegate.getWebletPath();
    }

    public String getContextPath() {
        return _delegate.getContextPath();
    }

    public String getPathInfo() {
        if (_shadowPathInfo != null) {
            return _shadowPathInfo;
        }
        return _delegate.getPathInfo();
    }

    public void setPathInfo(String pathInfo) {
        _shadowPathInfo = pathInfo;
    }

    public String getParameter(String name) {
        return _delegate.getParameter(name);
    }

    public Object getExternalRequest() {
        return _delegate.getExternalRequest();
    }

    public long getIfModifiedSince() {
        return _delegate.getIfModifiedSince();
    }
}
