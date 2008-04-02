package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletContainer;

import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.nio.CharBuffer;

/**
 * Text processing reader
 * which does the major text processing
 * from a given text reader
 * 
 */
public class TextProcessingReader extends BufferedReader {
    public static final Pattern _WEBLET_URL =
            Pattern.compile("weblet:\\s*url\\s*\\(\\s*[\"\\']([^\"\\']+)[\"\\']\\s*\\,\\s*[\"\\']([^\"\\'\\)]+)[\"\\']\\s*\\)?(.*)");
    public static final Pattern _WEBLET_RESOURCE =
            Pattern.compile("weblet:\\s*resource\\s*\\(\\s*[\"\\']([^\"\\'/]+)[\"\\']\\s*\\,\\s*[\"\\']([^\"\\'\\)]+)[\"\\']\\s*\\)?(.*)");


    BufferedReader r;

    StringBuffer processLineBuffer = new StringBuffer();
    StringBuffer readBuffer = null;

    String webletName;


    public TextProcessingReader(Reader r, String webletName) {
        super(r);
        this.r = new BufferedReader(r);
        this.webletName = webletName;
    }

    char[] lineBuf = new char[1];
    int linePos = -1;

    public int read() throws IOException {

        if (linePos == lineBuf.length - 1)
            linePos = -1;

        if (linePos == -1) {
            String line = readLine();
            if (lineBuf == null) return -1;
            lineBuf = line.toCharArray();
            if (lineBuf.length == 0) return -1;
        }
        linePos++;
        return lineBuf[linePos];
    }

    public int read(char[] chars, int i, int i1) throws IOException {
        char [] srcBuf = new char[i1-i];
        int readChars = read(srcBuf);
        if(readChars == -1)  return -1;
        System.arraycopy(srcBuf, 0, chars, i,readChars);
        return readChars;
    }


    boolean done = false;
    public void reset() throws IOException {
        super.reset();
        done = false;
        readBuffer = null;

    }

    public int read(char[] chars) throws IOException {
        if(done) return -1;

        if (readBuffer == null) {
            readBuffer = new StringBuffer(128);
        }
        do {
            if (readBuffer.length() < chars.length) {

                String line = readLine();
                if (line == null) {
                    char[] srcArr = readBuffer.toString().toCharArray();
                    System.arraycopy(srcArr, 0, chars, 0, srcArr.length);
                    done = true;
                    
                    return srcArr.length;
                } else {
                    readBuffer.append(line);
                }
            }

        } while (readBuffer.length() < chars.length);
        char[] srcArr = readBuffer.toString().toCharArray();

        System.arraycopy(srcArr, 0, chars, 0, chars.length);

        StringBuffer newReadBuffer = new StringBuffer(128);
        newReadBuffer.append(readBuffer.substring(chars.length));
        readBuffer = newReadBuffer;
        return chars.length;
    }

    public String readLine() throws IOException {
        String line = r.readLine();
        if (line == null) return line;
        line = processLine(line);
        return line;
    }


    private String processLine(String line) throws IOException {
        int startWebletUrl = 0;
        //int oldStartWebletUrl = -1;
        //int strLen = line.length();
        // do  {
        //if(startWebletUrl >=  strLen) break;
        ///oldStartWebletUrl = startWebletUrl;

        //TODO we have to replace this with a real ll1 parser this method has way too many
        //deficiencies

        startWebletUrl = line.substring(startWebletUrl).indexOf("weblet:url(");

        if (startWebletUrl != -1) {
            String protocol = line.substring(startWebletUrl);
            Matcher matcher = _WEBLET_URL.matcher(protocol);
            return resolveLine(line, startWebletUrl, matcher, true);
        } else if ((startWebletUrl = line.indexOf("weblet:resource(")) != -1) {
            String protocol = line.substring(startWebletUrl);
            Matcher matcher = _WEBLET_RESOURCE.matcher(protocol);
            return resolveLine(line, startWebletUrl, matcher, false);
        } else {
            return line + "\n";
        }
    }


    private String resolveLine(String line, int startAt, Matcher matcher, boolean url) throws IOException {
        StringBuffer buf = new StringBuffer(line.length() + 64);


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


            buf.append(preamble);


            buf.append(webletURL);
            buf.append(postamble);
            buf.append("\n");
        } else {
            buf.append(line);
            buf.append("\n");
        }
        return buf.toString();
    }


    public void close() throws IOException {
        r.close();
    }


}
