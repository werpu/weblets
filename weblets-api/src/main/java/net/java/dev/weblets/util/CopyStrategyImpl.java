package net.java.dev.weblets.util;

import java.io.*;

/**
 * @author werpu
 * @date: 22.09.2008
 */
public class CopyStrategyImpl implements CopyStrategy {
    public void copy(String webletName, String contentType, InputStream in, OutputStream out) throws IOException {
		boolean isText = isText(contentType);
		if (isText)
			copyText(webletName, new InputStreamReader(in), new OutputStreamWriter(out));
		else
			copyStream(in, out);
	}

	private boolean isText(String contentType) {
		return contentType != null && (contentType.startsWith("text/") || contentType.endsWith("xml") || contentType.equals("application/x-javascript"));
	}

	/**
	 * wraps the input stream from our given request into another input stream
	 *
	 * @param webletName  the name of the affected weblet
	 * @param mimetype the response mimetype
	 * @param in our given input steam
	 * @return  a wrapped input stream with our filterng cascade in place
	 * @throws IOException in case of an error
	 */
	public InputStream wrapInputStream(String webletName, String mimetype, InputStream in) throws IOException {
		if (isText(mimetype)) {
			BufferedReader bufIn = new BufferedReader(mapResponseReader(webletName, new InputStreamReader(in)));
			return new ReaderInputStream(bufIn);
		}
		return new BufferedInputStream(mapInputStream(in));
	}

	protected BufferedWriter mapResponseWriter(Writer out) {
		return new BufferedWriter(out);
	}

	protected BufferedReader mapResponseReader(String webletName, Reader in) {
		return new TextProcessingReader(in, webletName);
	}

	protected BufferedInputStream mapInputStream(InputStream in) {
		return new BufferedInputStream(in);
	}

	protected BufferedOutputStream mapOutputStream(OutputStream out) {
		return new BufferedOutputStream(out);
	}

	protected void copyText(String webletName, Reader in, Writer out) throws IOException {
		BufferedReader bufIn = mapResponseReader(webletName, in);
		BufferedWriter bufOut = mapResponseWriter(out);
		try {
			String line = null;
			while ((line = bufIn.readLine()) != null) {
				bufOut.write(line);
				bufOut.write("\n");
			}
		} finally {
			bufIn.close();
			bufOut.close();
		}
	}

	protected void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[2048];
		BufferedInputStream bufIn = mapInputStream(in);
		BufferedOutputStream bufOut = mapOutputStream(out);
		int len = 0;
		int total = 0;
		try {
			while ((len = bufIn.read(buffer)) > 0) {
				bufOut.write(buffer, 0, len);
				total += len;
			}
		} finally {
			bufIn.close();
			bufOut.close();
		}
	}
}
