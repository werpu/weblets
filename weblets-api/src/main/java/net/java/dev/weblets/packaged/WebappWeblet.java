package net.java.dev.weblets.packaged;

import net.java.dev.weblets.*;
import net.java.dev.weblets.resource.WebappResourceResolver;
import net.java.dev.weblets.packaged.ResourceloadingUtils;
import net.java.dev.weblets.util.CopyStrategy;
import net.java.dev.weblets.util.CopyStrategyImpl;
import net.java.dev.weblets.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Weblet for serving resources from a classical webapp structures This is currently only working in a portlet environment if weblets is triggered outside of
 * the portlet context so use it with care!
 *
 * @author Werner Punz
 */
public class WebappWeblet extends ServiceLoadingWeblet {

} 
