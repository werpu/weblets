package net.java.dev.weblets.demo.render.html;

import net.java.dev.weblets.FacesWebletUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * Renderer class
 * for our jsf custom component
 */
public class HtmlOutputCustomRenderer extends Renderer {
    private static final String WEBLETS_DEMO = "weblets.demo";
    private static final String WELCOME_JS = "/welcome.js";
    private static final String HMTL_SCRIPT = "script";
    private static final String HTML_TYPE = "type";
    private static final String HTML_SRC = "src";
    private static final String HTML_A = "a";
    private static final String HTML_HREF = "href";
    private static final String HTML_ONCLICK = "onclick";
    protected static final String HTML_JAVASCRIPT = "text/javascript";

    /**
     * Encode end for rendering our
     * component into the page
     *
     * @param context
     * @param component
     * @throws IOException
     */
    public void encodeEnd(
            FacesContext context,
            UIComponent component) throws IOException {
        //ViewHandler handler = context.getApplication().getViewHandler();
        UIOutput output = (UIOutput) component;
        Object value = output.getValue();
        String stringValue = null;
        if (value != null) {
            Converter converter = output.getConverter();
            if (converter != null)
                stringValue = converter.getAsString(context, output, value);
            else
                stringValue = value.toString();
        }
        ResponseWriter out = context.getResponseWriter();
        /**
         * We let weblets check if the resource already
         * is included if no then we can add our include string!
         */
        if (!FacesWebletUtils.isResourceLoaded(context, WEBLETS_DEMO, WELCOME_JS)) {
            /*We now fetch the resource and render it!*/
            String resourceURL = FacesWebletUtils.getURL(context, WEBLETS_DEMO, WELCOME_JS, true);
            out.startElement(HMTL_SCRIPT, component);
            out.writeAttribute(HTML_TYPE, HTML_JAVASCRIPT, null);
            out.writeAttribute(HTML_SRC, resourceURL, null);
            out.endElement(HMTL_SCRIPT);
        }
        if (stringValue != null) {
            out.startElement(HTML_A, component);
            out.writeAttribute(HTML_HREF, "#", null);
            out.writeAttribute(HTML_ONCLICK, "sayHello()", null);
            out.writeText(stringValue, null);
            out.endElement(HTML_A);
        }
    }
}