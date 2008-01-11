package net.java.dev.weblets.util;

/**
 * @author Werner Punz
 * Internal Contract for
 * Generic non jsf resource util classes
 * 
 */
public interface IWebletUtils {
    public String getResource(String weblet, String pathInfo);
    public String getURL(String weblet, String pathInfo);
}
