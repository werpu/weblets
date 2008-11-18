package net.java.dev.weblets.packaged;

import net.java.dev.weblets.*;
import net.java.dev.weblets.util.ServiceLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author werpu
 * @date: 18.11.2008
 * <p/>
 * Middle layer
 * the generic service loading
 * weblet, this one automates the service
 * loading layer
 * all service implementing weblets
 * are derived from this class!
 */
public class ServiceLoadingWeblet extends Weblet {
    Weblet _instance = null;



    public ServiceLoadingWeblet() {
        createInstance();
    }

    public int getWebletType() {
        return _instance.getWebletType();
    }

    /**
     * init method which is called by default to process the parameters
     *
     * @param config the webletconfig to be processed
     */
    public void init(WebletConfig config) {
        _instance.init(config);
    }

    public void service(WebletRequest request, WebletResponse response) throws IOException {
        _instance.service(request, response);
    }

    public WebletConfig getWebletConfig() {
        return _instance.getWebletConfig();
    }

    /**
     * Second Weblet entry point the service stream method is used internally for Weblets 1.1 by our asynchronous reporting interface <p/> It basically does the
     * same as service but must be servlet independend (aka it cannot rely on a base servlet or the external request of the weblet request object <p/> If you do
     * not trigger the reporting subengine then you can omit this interface it is not used internally
     *
     * @param pathInfo
     * @param mimetype
     * @return
     * @throws IOException
     * @throws net.java.dev.weblets.WebletException
     *
     */
    public InputStream serviceStream(String pathInfo, String mimetype) throws IOException, WebletException {
        return _instance.serviceStream(pathInfo, mimetype);
    }

    public void destroy() {
        _instance.destroy();
    }

    /**
     * creates a new instance for this contract
     * the service class is PackagedWebletImpl in the impl
     * subproject!
     * <p/>
     * The binding is done via META-INF/services!
     */
    private void createInstance() {
        if (_instance == null) {
            synchronized (getClass()) {
                if (_instance != null) {
                    return;
                }
                Class resourceClass = ServiceLoader.loadService(getClass());
                try {
                    _instance = (Weblet) resourceClass.newInstance();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to access " + "WebletsContextListener implementation", e);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Unable to instantiate " + "WebletsContextListener implementation", e);
                }
            }
        }
    }
}
