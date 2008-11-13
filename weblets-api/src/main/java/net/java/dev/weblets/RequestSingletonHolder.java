package net.java.dev.weblets;

/**
 * @author werpu
 * @date: 13.11.2008
 * <p/>
 * Supporting interface which can be used
 * to implement a custom singleton holder
 * normally a normal request object
 * should do it, but you can use
 * this interface to implement your own
 * custom singleton holder
 * in case a request is not available!
 * <p/>
 * Note this class is not referenced directly
 * in the code, instead introspection is used
 * because we cannot rely on every request
 * singleton holder to implement this interface
 * portlet or servlet requests do not implement it
 * for instance!
 */
public interface RequestSingletonHolder {

    /**
     * gets an attribute if present
     *
     * @param key the attribute key
     * @return the attribute or null if none is set
     */
    public Object getAttribute(String key);

    /**
     * sets an attribute with a given key and value, existing attributes will be replaced
     * if none exists a new one will be set
     *
     * @param key   the attribute key
     * @param value the attribute value
     */
    public void setAttribute(String key, Object value);
}
