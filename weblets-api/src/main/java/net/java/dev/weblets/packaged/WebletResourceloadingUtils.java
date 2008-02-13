package net.java.dev.weblets.packaged;

import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.util.IStreamingFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

/**
 * helper class to be shared by various weblet loaders
 */
public class WebletResourceloadingUtils {
    static WebletResourceloadingUtils instance = new WebletResourceloadingUtils();

    public static WebletResourceloadingUtils getInstance() {
        return instance;
    }


    public void loadFromUrl(WebletConfig config, WebletRequest request, WebletResponse response, URL url, IStreamingFilter filterChain) throws IOException {
        if (url != null) {

            URLConnection conn = url.openConnection();
            long never = -1;
            if (config.getWebletVersion() == null || config.getWebletVersion().trim().equals(""))
                response.setLastModified(conn.getLastModified());
            else {
                //we lock out resource loading once versioned, we do not run into the chain
                //just in case the expires is ignored
                //this enforces the loading from cache on some browsers
                //even if refresh is pressed, this is by
                //definition the wanted behavior if versioning is on!
                //some browsers like firefox despite
                //having a future number pass a local date on refresh maybe we lock this out as well
                long now = System.currentTimeMillis();
                never = now + 100l * 60l * 60l * 24l * 365l;
                response.setLastModified(never);


                //this should prevent requests entirely!
                response.setContentVersion(config.getWebletVersion());
            }
            response.setContentType(null); // Bogus "text/html" overriding mime-type

            //some browsers only work on seconds  (Mozilla)  so we go down to one second for a shared
            //common response time
            //we cannot tamper the cache state here, because
            //otherwise firefox will fail with an emptied page resource cache (shift f5)
            long requestCacheState = request.getIfModifiedSince() ;
       
            if (requestCacheState > 1000)
                requestCacheState = requestCacheState - requestCacheState % 1000;
            long responseCacheState = conn.getLastModified();
            if (responseCacheState > 1000)
                responseCacheState = responseCacheState - responseCacheState % 1000;

            if (requestCacheState < responseCacheState) {
                InputStream in = conn.getInputStream();
                OutputStream out = response.getOutputStream();
                try {
                    filterChain.filter(config, request, response, in, out);
                } finally {
                    filterChain.close();
                    //just in case we missed something in the lower rim
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log log = LogFactory.getLog(this.getClass());
                        log.error(e);
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        Log log = LogFactory.getLog(this.getClass());
                        log.error(e);
                    }
                }
            } else {
                response.setStatus(WebletResponse.SC_NOT_MODIFIED);
            }
        } else {
            response.setStatus(WebletResponse.SC_NOT_FOUND);
        }
    }

}
