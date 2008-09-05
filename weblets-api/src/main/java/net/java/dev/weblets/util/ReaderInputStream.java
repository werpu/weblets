package net.java.dev.weblets.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * Maps a reader again into an input stream this way we can remap text processing readers and writers again into streams to be prcessed from the outside
 */
public class ReaderInputStream extends InputStream {
	private final Reader			reader;
	private final Writer			writer;
	private final PipedInputStream	inputStream;
	private static final int		COPY_BUFFER	= 4096;

	public ReaderInputStream(Reader reader) throws IOException {
		this(reader, null);
	}

	public ReaderInputStream(final Reader reader, String encoding) throws IOException {
		synchronized (this) {
			this.reader = reader;
			inputStream = new PipedInputStream();
			OutputStream outputStream = new PipedOutputStream(inputStream);
			writer = (encoding == null) ? new OutputStreamWriter(outputStream) : new OutputStreamWriter(outputStream, encoding);
		}
		new Thread(new PipeThread()).start();
	}

	public int read() throws IOException {
		return inputStream.read();
	}

	public int read(byte b[]) throws IOException {
		return inputStream.read(b);
	}

	public int read(byte b[], int off, int len) throws IOException {
		return inputStream.read(b, off, len);
	}

	public long skip(long n) throws IOException {
		return inputStream.skip(n);
	}

	public int available() throws IOException {
		return inputStream.available();
	}

	public synchronized void close() throws IOException {
		ReaderInputStream.close(reader);
		ReaderInputStream.close(writer);
		ReaderInputStream.close(inputStream);
	}

	private static void close(Closeable val) throws IOException {
		if (val != null)
			val.close();
	}

	class PipeThread implements Runnable {
		public void run() {
			char[] buffer = new char[COPY_BUFFER];
			int n = -1;
			try {
				if (reader != null && writer != null) {
					do {
						synchronized (ReaderInputStream.this) {
							n = reader.read(buffer);
							if (n != -1) {
								writer.write(buffer, 0, n);
								writer.flush();
							}
						}
					} while (n != -1);
				}
			} catch (IOException e) {
				Log log = LogFactory.getLog(PipeThread.class);
				log.error(e);
			} finally {
				try {
					close(reader);
				} catch (IOException e) {
					Log log = LogFactory.getLog(PipeThread.class);
					log.error(e);
				}
				try {
					close(writer);
				} catch (IOException e) {
					Log log = LogFactory.getLog(PipeThread.class);
					log.error(e);
				}
			}
		}
	}
}