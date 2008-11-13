package net.java.dev.weblets.demo.sourcecodeweblet;

import net.java.dev.weblets.WebletUtils;
import net.java.dev.weblets.util.CopyStrategy;
import net.java.dev.weblets.util.CopyStrategyImpl;

import java.io.*;

/**
 * Sourcecode decorating copy strategy
 * for the beautification of the sources
 */
public class SourcecodeCopyStrategy extends CopyStrategyImpl implements CopyStrategy {

    public void copy(String webletName, String mimeType, InputStream in, OutputStream out) throws IOException {
        copyText(webletName, new InputStreamReader(in), new OutputStreamWriter(out));
    }

    protected void copyText(String webletName, Reader in, Writer out) throws IOException {
        BufferedReader bufIn = new BufferedReader(mapResponseReader(webletName, in));
        PrintWriter bufOut = new PrintWriter(mapResponseWriter(out));
        try {
            writehttphead(bufOut);
            writeResource(bufIn, bufOut);
            writehttpbottom(bufOut);
        } finally {
            bufIn.close();
            bufOut.close();
        }
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
