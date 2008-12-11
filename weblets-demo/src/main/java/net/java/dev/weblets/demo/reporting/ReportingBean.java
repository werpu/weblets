package net.java.dev.weblets.demo.reporting;

import net.java.dev.weblets.WebletUtils;

import java.io.*;

/**
 * @author werpu
 * @date: 14.10.2008
 *
 * a helper class triggering the reporting subsystem
 * testing for its validity within the scope of a running
 * weblets container!
 *
 *
 * This is a very basic test ensuring that the reporting subsystem triggers correctly
 * without errors!
 * 
 */
public class ReportingBean {

    public String getReportingString() {
       InputStream istr =  WebletUtils.getResourceAsStream("weblets.demo", "/welcome.js", "text/javascript");
       BufferedReader bufread = new BufferedReader(new InputStreamReader(istr));


       String currLine = null;
       StringBuffer strBuf = new StringBuffer();
       try {
            while((currLine = bufread.readLine() )!= null) {
               strBuf.append(currLine);
               strBuf.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           try {
               bufread.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       String finalString = strBuf.toString();
       if(finalString.contains("alert('/weblets-demo/weblets/weblets/demo$1.0/welcome.js')")) {
           return "reportingtest successful";
       } else {
           return "reportingtest fails!!";
       }
    }
}
