package net.java.dev.weblets.demo.component.html;

import javax.faces.component.UIOutput;

/**
 * The HtmlOutputCustom component.
 */
public class HtmlOutputCustom extends UIOutput {
    /**
     * The component type for this component.
     */
    public static final String COMPONENT_TYPE = "net.java.dev.weblets.demo.HtmlOutputCustom";

    /**
     * The renderer type for this component.
     */
    public static final String RENDERER_TYPE = "net.java.dev.weblets.demo.Custom";

    /**
     * Creates a new HtmlOutputCustom.
     */
    public HtmlOutputCustom() {
        setRendererType(RENDERER_TYPE);
    }
}
