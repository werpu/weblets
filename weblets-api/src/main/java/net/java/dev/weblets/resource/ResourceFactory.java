package net.java.dev.weblets.resource;

import net.java.dev.weblets.WebletRequest;

import java.io.IOException;

/**
 * @author werpu
 * @date: 10.11.2008
 * <p/>
 * The resourcefactory
 * is a factory class which
 * maps a request into a resource
 * given its internal conditions also
 * caches the resources for later usage
 * <p/>
 * the mapping itself is done by the resourceresolver
 */
public interface ResourceFactory {

    /**
     * standard get method for the factory
     *
     * @param request          the incoming request
     * @param resourceResolver the resource resolver
     * @param resolveBundle    if set to true the resource is resolved into its parent bundle if embedded!
     * @return a resource either new or recycled depending on the implementation
     * @throws IOException
     */
    public WebletResource getResource(WebletRequest request, ResourceResolver resourceResolver, boolean resolveBundle) throws IOException;
}
