package net.java.dev.weblets.demo.component.html;

import javax.faces.component.UIOutput;

/**
 * @author werpu
 * @date: 17.11.2008
 */
public class HtmlOutputBundle extends UIOutput {
    /**
     * The component type for this component.
     */
    public static final String COMPONENT_TYPE = "net.java.dev.weblets.demo.HtmlOutputBundle";

    /**
     * The renderer type for this component.
     */
    public static final String RENDERER_TYPE = "net.java.dev.weblets.demo.Bundle";

    /**
     * Creates a new HtmlOutputCustom.
     */
    public HtmlOutputBundle() {
        setRendererType(RENDERER_TYPE);
    }
}
