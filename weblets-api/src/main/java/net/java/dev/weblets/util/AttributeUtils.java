package net.java.dev.weblets.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utils class handling the attribute utils
 * TODO move this into an API-IMPLED generic
 * ReflectUtils!
 */
public class AttributeUtils {

    public static Object getAttribute(Object implementor, String name) {
        // return _httpResponse.getOutputStream();
        Method m = null;
        try {
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            m = implementor.getClass().getMethod("getAttribute", paramTypes);
            try {
                String[] params = new String[1];
                params[0] = name;
                return m.invoke(implementor, params);
            } catch (IllegalAccessException e) {
                Log log = LogFactory.getLog(AttributeUtils.class);
                log.error(e);
            } catch (InvocationTargetException e) {
                Log log = LogFactory.getLog(AttributeUtils.class);
                log.error(e);
            }
            return null;
        } catch (NoSuchMethodException e) {
            Log log = LogFactory.getLog(AttributeUtils.class);
            log.error(e);
        }
        return null;
    }

    public static void setAttribute(Object implementor, String name, Object value) {
        // return _httpResponse.getOutputStream();
        Method m = null;
        try {
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = Object.class;
            m = implementor.getClass().getMethod("setAttribute", paramTypes);
            try {
                Object[] params = new Object[2];
                params[0] = name;
                params[1] = value;
                m.invoke(implementor, params);
            } catch (IllegalAccessException e) {
                Log log = LogFactory.getLog(AttributeUtils.class);
                log.error(e);
            } catch (InvocationTargetException e) {
                Log log = LogFactory.getLog(AttributeUtils.class);
                log.error(e);
            }
        } catch (NoSuchMethodException e) {
            Log log = LogFactory.getLog(AttributeUtils.class);
            log.error(e);
        }
    }
}
