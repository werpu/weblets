package net.java.dev.weblets.demo.sourcecodeweblet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.WebletUtils;
import net.java.dev.weblets.util.FilterThread;
import net.java.dev.weblets.util.StreamingFilter;

/**
 * A basic regxp bases text processing filter doing the weblet resource url
 * replacement parsing
 * 
 * <p>
 * note: this class is not thread save, it always should be used in a single
 * thread only! The same goes for the reference chains the threading of the
 * referenced chain elements is enforced by the java pipe api and does not
 * disrupt this limitation!
 * </p>
 * 
 * @author: Werner Punz
 * @date: 03.01.2008.
 */
public class WebletsSourcepageFilter extends StreamingFilter {

	public void addFilter(StreamingFilter filter) {
		setParentFilter(filter);
	}

	private boolean isTriggered(WebletRequest request, WebletResponse response) {
		return true;
	}

	public void filter(WebletConfig config, WebletRequest request, WebletResponse response, InputStream in, OutputStream out) throws IOException {
		

		BufferedReader reader = null;
		reader = processParentFilter(config, request, response, in);
		PrintWriter writer = new PrintWriter(new BufferedOutputStream(out));
		setCloseWriter(writer);

		writehttphead(writer);
		writeResource(reader, writer);
		writehttpbottom(writer);
		writer.flush();
	}

	private BufferedReader processParentFilter(WebletConfig config, WebletRequest request, WebletResponse response, InputStream in) throws IOException {
		BufferedReader reader;
		if (getParentFilter() != null) {
			setCloseInputStream(in); 
			/*
			 * we have a break in the input oput
			 * chain due to the pipe
			 */
			PipedOutputStream pipeOut = new PipedOutputStream();
			reader = new BufferedReader(new InputStreamReader(new PipedInputStream(pipeOut)));
			setCloseReader(reader);
			Thread parentFilterThread = new FilterThread(getParentFilter(), config, request, response, in, pipeOut);
			parentFilterThread.start();
		} else {
			reader = new BufferedReader(new InputStreamReader(in));
			setCloseReader(reader);
		}
		return reader;
	}

	private void writehttpbottom(PrintWriter writer) {
		writer.write("\n");
		writer.write("</pre></div></body></html>");
	}

	private void writeResource(BufferedReader reader, PrintWriter writer) throws IOException {
		while (reader.ready()) {
			String line = reader.readLine();
			line = line.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			writer.write(line);
			writer.println();
		}
	}

	private void writehttphead(PrintWriter writer) {
		writer.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		writer.write("<html><head>");
		writer.write("<link rel=\"stylesheet\" href=\"");
		writer.write(WebletUtils.getURL("weblets.demo", "/styles/weblets.css"));
		writer.write("\" ></link>");
		writer.write("</head><body><div class=\"header_bg\" /><div class=\"content\"><pre>");
		writer.write("\n");
	}

}
