package net.java.dev.weblets.demo.sourcecodeweblet;

import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.WebletUtils;
import net.java.dev.weblets.util.IWebletUtils;
import net.java.dev.weblets.util.CopyProviderImpl;
import net.java.dev.weblets.util.CopyProvider;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: werpu
 * Date: 20.03.2008
 * Time: 18:57:43
 * To change this template use File | Settings | File Templates.
 */
public class SourcecodeCopyProvider extends CopyProviderImpl implements CopyProvider {

       public void copy(WebletRequest request, String mimeType, InputStream in, OutputStream out) throws IOException {


            copyText(request,  new InputStreamReader(in), new OutputStreamWriter(out));
        
        }


    protected void copyText(WebletRequest request,  Reader in, Writer out) throws IOException {
        byte[] buffer = new byte[2048];

        int len = 0;
        int total = 0;
        BufferedReader bufIn = new BufferedReader(mapResponseReader(request,  in));
        PrintWriter bufOut = new PrintWriter(mapResponseWriter(request,  out));
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
