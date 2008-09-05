package net.java.dev.weblets.sandbox;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.java.dev.weblets.Weblet;
import net.java.dev.weblets.WebletConfig;
import net.java.dev.weblets.WebletException;
import net.java.dev.weblets.WebletRequest;
import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.packaged.WebletResourceloadingUtils;
import net.java.dev.weblets.util.CopyProvider;
import net.java.dev.weblets.util.CopyProviderImpl;
import net.java.dev.weblets.util.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Weblet for serving resources from a classical webapp structures This is currently only working in a portlet environment if weblets is triggered outside of
 * the portlet context so use it with care!
 * 
 * 
 * 
 * @author Werner Punz
 * 
 */
public class WebappWeblet extends Weblet {
	Set				_allowedResources	= null;
	String			_packageName		= "";
	private String	_resourceRoot;

	/**
	 * init method which is called by default to process the parameters
	 * 
	 * @param config
	 *            the webletconfig to be processed
	 */
	public void init(WebletConfig config) {
		super.init(config);
		// fetch the weblets init param
		_packageName = config.getInitParameter("package");
		// fetch the resource root param
		_resourceRoot = config.getInitParameter("resourceRoot");
		// init param missing, lets throw an error
		if (_packageName == null && _resourceRoot == null) {
			throw new WebletException("Missing either init parameter \"package\" or " + " or init parameter \"resourceRoot\" for " + " Weblet \""
					+ config.getWebletName() + "\"");
		}
		// the init was successful we now have all we need
		_resourceRoot = (_packageName != null) ? _packageName.replace('.', '/') : _resourceRoot;
		/*
		 * optional inclusion list, if not set all files are served which are in valid resource dirs
		 */
		initAllowedFiletypes(config);
	}

	/**
	 * initialization code for the allowed filetypes list
	 * @param config
	 */
	private void initAllowedFiletypes(WebletConfig config) {
		String allowedFiletypes = config.getInitParameter("allowedResources");
		// we now determine the allowed mime types for this weblet
		if (!StringUtils.isBlank(allowedFiletypes)) {
			String[] filetypesArr = allowedFiletypes.split("[\\,\\;]");
			for (int cnt = 0; cnt < filetypesArr.length; cnt++) {
				String fileType = filetypesArr[cnt];
				fileType = fileType.replaceAll("\\*", "");
				fileType = fileType.replaceAll("\\.", "");
				String mimeType = config.getMimeType(fileType);
				if (!StringUtils.isBlank(mimeType)) {
					if (_allowedResources == null) {
						_allowedResources = new HashSet();
					}
					_allowedResources.add(mimeType);
				} else {
					Log logger = LogFactory.getLog(this.getClass());
					logger.warn("Mimetype of type:" + mimeType
							+ " was not defined please check your container settings or set the mimetype in your weblet definition");
				}
			}
		}
	}

	public void service(WebletRequest request, WebletResponse response) throws IOException {
		String resourcePath = _resourceRoot + request.getPathInfo();
		// WebletRequestImpl webletRequest = (WebletRequestImpl) request;
		// this might fail on some containers overriding the HttpServlet
		// but for demo purposes this is ok
		HttpServletRequest httpRequest = (HttpServletRequest) request.getExternalRequest();
		// note, please do not use the filter api for your own filters
		// yet it will be substantially reworked in 1.1 after that
		// it will be fully documented and opened for third party
		// extensions, the current piping api is to complicated
		CopyProvider copyProvider = new CopyProviderImpl();
		if (resourcePath.indexOf("WEB-INF/") != -1 || resourcePath.indexOf("WEB-INF\\") != -1) {
			// security breach nothing in WEB-INF is allowed to be accessed
			return;
		}
		if (_allowedResources != null) {
			String mimeType = super.getWebletConfig().getMimeType(resourcePath);
			if (!_allowedResources.contains(mimeType)) {
				return;/* not allowed no content delivered */
			}
		}
		URL url = httpRequest.getSession().getServletContext().getResource(resourcePath);
		WebletResourceloadingUtils.getInstance().loadFromUrl(getWebletConfig(), request, response, url, copyProvider);
	}

	public InputStream serviceStream(String pathInfo, String mimetype) throws IOException, WebletException {
		return null; // To change body of implemented methods use File |
		// Settings | File Templates.
	}

	public void destroy() {
		_resourceRoot = null;
		_packageName = null;
		_allowedResources = null;
		super.destroy();
	}
}
