package net.java.dev.weblets.packaged;

import net.java.dev.weblets.*;
import net.java.dev.weblets.util.CopyStrategy;
import net.java.dev.weblets.util.CopyStrategyImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * weblet which streams resources from another valid url vfs location
 * <p/>
 * it sort of acts as proxy for remote url resources and can hide the origin of the original weblets
 * <p/>
 * note! this stream is under construction and experimental only, it should not be used for production
 */
public class URLWeblet extends ServiceLoadingWeblet {

}
