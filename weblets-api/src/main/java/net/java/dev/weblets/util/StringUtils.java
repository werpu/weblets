package net.java.dev.weblets.util;

/**
 * Our own StringUtils class to 
 * reduce the dependency into commons
 * lang in the long run
 * @author Werner Punz
 *
 */
public class StringUtils {
	
	public static final boolean isBlank(String in) {
		return (in == null || in.trim().equals(""));
	}
	
}
