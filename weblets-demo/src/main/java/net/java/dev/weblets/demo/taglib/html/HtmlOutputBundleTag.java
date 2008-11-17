package net.java.dev.weblets.demo.taglib.html;

import net.java.dev.weblets.demo.component.html.HtmlOutputBundle;

/**
 * @author werpu
 * @date: 17.11.2008
 * <p/>
 * Tag file for our bundle testing
 * component which does some bundeling of resources
 */
public class HtmlOutputBundleTag extends HtmlOutputCustomTag {
    /**
     * Returns the component type.
     *
     * @return the component type
     */
    public String getComponentType() {
        return HtmlOutputBundle.COMPONENT_TYPE;
    }

    /**
     * Returns the renderer type.
     *
     * @return the renderer type
     */
    public String getRendererType() {
        return HtmlOutputBundle.RENDERER_TYPE;
    }
}
