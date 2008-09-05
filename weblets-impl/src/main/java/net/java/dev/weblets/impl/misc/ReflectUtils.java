package net.java.dev.weblets.impl.misc;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import net.java.dev.weblets.impl.WebletsContextListenerImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReflectUtils {
	public static String calculateContextPath(WebletsContextListenerImpl.WebXmlParser parser, ServletContext context) {
		String contextPath;
		contextPath = parser.getWebletsContextPath();
		if (contextPath == null || contextPath.trim().equals("")) {
			try {
				// lets check if we are in JEE 5 so that we can execute a
				// getServletContextPath methid
				Method[] methods = context.getClass().getMethods();
				for (int cnt = 0; cnt < methods.length; cnt++) {
					if (methods[cnt].getName().equals("getContextPath")) {
						return (String) methods[cnt].invoke(context, null);
					}
				}
			} catch (IllegalAccessException e) {
				Log log = LogFactory.getLog(WebletsContextListenerImpl.class);
				log.error("Error, trying to invoke getContextPath ", e);
			} catch (InvocationTargetException e) {
				Log log = LogFactory.getLog(WebletsContextListenerImpl.class);
				log.error("Error, trying to invoke getContextPath ", e);
			}
		} else {
			return contextPath;
		}
		return "";
	}

	public static OutputStream getOutputStream(Object response) throws IOException {
		// return _httpResponse.getOutputStream();
		Method m = null;
		try {
			m = response.getClass().getMethod("getOutputStream", (Class[]) null);
			try {
				return (OutputStream) m.invoke(response, new Class[] {});
			} catch (IllegalAccessException e) {
				Log log = LogFactory.getLog(ReflectUtils.class);
				log.error(e);
			} catch (InvocationTargetException e) {
				Log log = LogFactory.getLog(ReflectUtils.class);
				log.error(e);
			}
			return null;
		} catch (NoSuchMethodException e) {
			try {
				// this should work because we are in a prerender stage but already
				// have the response object
				// this needs further testing of course!
				m = response.getClass().getMethod("getPortletOutputStream", (Class[]) null);
				try {
					return (OutputStream) m.invoke(response, new Class[] {});
				} catch (IllegalAccessException e1) {
					Log log = LogFactory.getLog(ReflectUtils.class);
					log.error(e1);
				} catch (InvocationTargetException e2) {
					Log log = LogFactory.getLog(ReflectUtils.class);
					log.error(e2);
				}
				return null;
			} catch (NoSuchMethodException ex) {
				Log log = LogFactory.getLog(ReflectUtils.class);
				log.error(ex);
			}
		}
		return null;
	}

	public static String getParameter(Object response, String name) {
		// return _httpResponse.getOutputStream();
		Method m = null;
		try {
			m = response.getClass().getMethod("getParameter", (Class[]) null);
			try {
				String[] params = new String[1];
				params[0] = name;
				return (String) m.invoke(response, params);
			} catch (IllegalAccessException e) {
				Log log = LogFactory.getLog(ReflectUtils.class);
				log.error(e);
			} catch (InvocationTargetException e) {
				Log log = LogFactory.getLog(ReflectUtils.class);
				log.error(e);
			}
			return null;
		} catch (NoSuchMethodException e) {
			Log log = LogFactory.getLog(ReflectUtils.class);
			log.error(e);
		}
		return null;
	}
}
