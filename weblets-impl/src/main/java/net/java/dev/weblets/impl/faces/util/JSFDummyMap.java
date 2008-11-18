/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.dev.weblets.impl.faces.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author werpu
 */
public abstract class JSFDummyMap implements Map {

    public int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abstract Object get(Object key);

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void putAll(Map m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection values() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
