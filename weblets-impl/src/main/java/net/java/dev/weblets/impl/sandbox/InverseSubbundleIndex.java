package net.java.dev.weblets.impl.sandbox;

import net.java.dev.weblets.sandbox.Subbundle;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

/**
 * @author werpu
 * @date: 03.11.2008
 * <p/>
 * High performance inverse subbundle
 * reverse lookup
 * index for faster lookups of resources!
 */
public class InverseSubbundleIndex {

    Map/*<ResourceId, Subbundle>*/ _inverseIndex = new HashMap();
    Map/*<BundleId, Bundle>*/ _subbundles = new HashMap();

    public Collection getSubbundles() {
        return _subbundles.values();
    }

    public void addBundle(String resource, Subbundle subbundle) {
        synchronized (this) {   //for add we have to lock!
            _inverseIndex.put(resource, subbundle);
            _subbundles.put(subbundle.getSubbundleId(), subbundle);
            subbundle.getResources().add(resource);
           
        }
    }

    public void addBundle(Subbundle subbundle) {
        synchronized (this) {
            Iterator it = subbundle.getResources().iterator();
            while (it.hasNext()) {
                _subbundles.put((String) it.next(), subbundle);
            }
            _subbundles.put(subbundle.getSubbundleId(), subbundle);
        }
    }

    /**
     * gets a valid bundle from a given resource id
     *
     * @param resource the resource id to be used
     * @return a valid subbundle or null if none was found
     */
    public Subbundle findBundleFromResource(String resource) {
        return (Subbundle) _inverseIndex.get(resource);
    }

    /**
     * fetches the subbundle from the given subbundle id
     *
     * @param bundleId the bundle id to be used
     * @return a valid subbundle or null if none was found!
     */
    public Subbundle findBundleFromId(String bundleId) {
        return (Subbundle) _subbundles.get(bundleId);
    }
}