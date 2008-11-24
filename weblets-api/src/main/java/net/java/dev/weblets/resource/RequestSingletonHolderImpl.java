package net.java.dev.weblets.resource;

import net.java.dev.weblets.RequestSingletonHolder;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author werpu
 * @date: 24.11.2008
 *
 * This is a needed class
 * because acegi and trinidas seem
 * to go haiwire on their own
 * request facades, we misuse
 * the jsf managed bean facilities
 * to roll our own request based bean to hold the values
 * a set and get attribute does not work properly
 *
 */
public class RequestSingletonHolderImpl implements RequestSingletonHolder {

    Map singletinHolder = Collections.synchronizedMap(new HashMap());

    public Object getAttribute(String key) {
        return singletinHolder.get(key);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAttribute(String key, Object value) {
        singletinHolder.put(key, value);
    }
}
