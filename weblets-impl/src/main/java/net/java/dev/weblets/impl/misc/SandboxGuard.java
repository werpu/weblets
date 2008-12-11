package net.java.dev.weblets.impl.misc;

import net.java.dev.weblets.WebletRequest;

/**
 * A security enabler to jail our resources so that no request can break out of our jailed paths!
 * 
 */
public class SandboxGuard {
	/**
	 * we can jail our resources by blocking requests which try to break through our resource root
	 * 
	 * @param origResourcePath
	 *            the original resource path which will be checked for futile patterns
	 * 
	 * @return true in case of a detected jailbreak false if not
	 */
	public static boolean isJailBreak(String origResourcePath) {
		origResourcePath = origResourcePath.trim();
		int startSubstr = 0;
		int endSubstr = origResourcePath.length();
		if (origResourcePath.startsWith("/"))
			startSubstr++;
		if (origResourcePath.endsWith("/"))
			endSubstr--;
		if (startSubstr < endSubstr)
			origResourcePath = origResourcePath.substring(startSubstr, endSubstr);
		String[] elements = origResourcePath.split("/");
		int nonBackpath = 0;
		int backPath = 0;
		int interimbreak = 1;
		int len = elements.length;
		for (int cnt = 0; cnt < len; cnt++) {
			// check for empty values and double quotes
			// TODO empty value security check investigate what it does pathwise
			String pathEntry = elements[cnt].trim();
			if (pathEntry.equals(".")) {
				// do nothing this is a zeroconf entry
			} else if (pathEntry.equals("..")) {
				backPath++;
				interimbreak--;
			} else {
				nonBackpath++;
				interimbreak++;
			}
			if (interimbreak == 0)
				return true;
			/*
			 * we stepped out of our given root this is a clear break you can jump into a sidedir that way
			 */
		}
		return nonBackpath <= backPath;
	}
}
