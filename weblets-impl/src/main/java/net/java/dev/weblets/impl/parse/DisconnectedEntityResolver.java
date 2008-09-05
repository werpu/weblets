package net.java.dev.weblets.impl.parse;

import java.io.ByteArrayInputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * DisconnectedEntityResolver prevents external network access during parsing in case the remote host cannot be reached.
 */
public class DisconnectedEntityResolver implements EntityResolver {
	public InputSource resolveEntity(String publicId, String systemId) {
		// use an empty input source
		return new InputSource(new ByteArrayInputStream(new byte[0]));
	}

	// no instances
	private DisconnectedEntityResolver() {
	}

	static public DisconnectedEntityResolver sharedInstance() {
		return _INSTANCE;
	}

	static private DisconnectedEntityResolver	_INSTANCE	= new DisconnectedEntityResolver();
}
