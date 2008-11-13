package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletContainer;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * our basic text processing writer it does the basic parsing of our resources and the weblet: to url substitution for our resources
 */
class TextProcessingWriter extends Writer {
	public static final Pattern	_WEBLET_URL			= Pattern
															.compile("weblet:\\s*url\\s*\\(\\s*[\"\\']([^\"\\']+)[\"\\']\\s*\\,\\s*[\"\\']([^\"\\'\\)]+)[\"\\']\\s*\\)?(.*)");
	public static final Pattern	_WEBLET_RESOURCE	= Pattern
															.compile("weblet:\\s*resource\\s*\\(\\s*[\"\\']([^\"\\'/]+)[\"\\']\\s*\\,\\s*[\"\\']([^\"\\'\\)]+)[\"\\']\\s*\\)?(.*)");
	Writer						r;
	int							read				= 0;
	StringBuffer				buf					= new StringBuffer();
	String						webletName;
	int							offShift			= 0;

	public TextProcessingWriter(Writer r, String webletName) {
		super();
		this.r = r;
		this.webletName = webletName;
	}

	public void write(char[] chars, int off, int len) throws IOException {
		if (chars == null)
			throw new IOException("Null array");
		// we buffer line by line and then do the text processing
		String currentBuf = new String(chars).substring(off, len);
		int linePos = -1;
		int oldLinePos = 0;
		currentBuf = currentBuf.replaceAll("\r", ""); /* we skip the unwanted windows linebreak additions */
		while (linePos < (currentBuf.length() - 1) && (linePos = currentBuf.substring(linePos + 1).indexOf('\n')) != -1) {
			linePos = linePos + oldLinePos;
			buf.append(currentBuf.substring(oldLinePos, linePos));
			processLine(buf.toString());
			oldLinePos = linePos + 1;
			buf = new StringBuffer();
		}
		if (oldLinePos < currentBuf.length())
			buf.append(currentBuf.substring(oldLinePos));
		// To change body of implemented methods use File | Settings | File Templates.
	}

	private void processLine(String line) throws IOException {
		int startWebletUrl = 0;
		// int oldStartWebletUrl = -1;
		// int strLen = line.length();
		// do {
		// if(startWebletUrl >= strLen) break;
		// /oldStartWebletUrl = startWebletUrl;
		// TODO we have to replace this with a real ll1 parser this method has way too many
		// deficiencies
		startWebletUrl = line.substring(startWebletUrl).indexOf("weblet:url(");
		if (startWebletUrl != -1) {
			String protocol = line.substring(startWebletUrl);
			Matcher matcher = _WEBLET_URL.matcher(protocol);
			resolveLine(line, startWebletUrl, matcher, true);
		} else if ((startWebletUrl = line.indexOf("weblet:resource(")) != -1) {
			String protocol = line.substring(startWebletUrl);
			Matcher matcher = _WEBLET_RESOURCE.matcher(protocol);
			resolveLine(line, startWebletUrl, matcher, false);
		} else {
			r.write(line);
			r.write("\n");
		}
		// } while(startWebletUrl != -1);
	}

	private void resolveLine(String line, int startAt, Matcher matcher, boolean url) throws IOException {
		if (matcher.matches()) {
			String preamble = line.substring(0, startAt);
			String webletName = matcher.group(1);
			webletName = (webletName != null) ? webletName.trim() : webletName;
			String pathInfo = matcher.group(2);
			pathInfo = (pathInfo != null) ? pathInfo.trim() : pathInfo;
			String postamble = matcher.group(3);
			// default relative weblet:/resource.ext to this weblet
			if (webletName == null) {
				webletName = this.webletName;
			}
			WebletContainer container = WebletContainer.getInstance();
			String webletURL = null;
			if (url)
				webletURL = container.getWebletContextPath() + container.getResourceUri(webletName, pathInfo);
			else
				webletURL = container.getResourceUri(webletName, pathInfo);
			r.write(preamble);
			r.write(webletURL);
			r.write(postamble);
			r.write("\n");
		} else {
			r.write(line);
			r.write("\n");
		}
	}

	public void flush() throws IOException {
		r.flush();
	}

	public void close() throws IOException {
		// To change body of implemented methods use File | Settings | File Templates.
		try {
			r.write(buf.toString());
			r.flush();
		} finally {
			r.close();
		}
	}


}
