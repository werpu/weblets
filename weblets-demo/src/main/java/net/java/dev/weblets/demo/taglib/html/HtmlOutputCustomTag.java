package net.java.dev.weblets.demo.taglib.html;

import javax.faces.component.UIComponent;

import net.java.dev.weblets.demo.component.html.HtmlOutputCustom;
import net.java.dev.weblets.demo.taglib.UIComponentTagSupport;

/**
 * HtmlOutputCustomTag component tag handler.
 */
public class HtmlOutputCustomTag extends UIComponentTagSupport {
    /**
     * Returns the component type.
     *
     * @return the component type
     */
    public String getComponentType() {
        return HtmlOutputCustom.COMPONENT_TYPE;
    }

    /**
     * Returns the renderer type.
     *
     * @return the renderer type
     */
    public String getRendererType() {
        return HtmlOutputCustom.RENDERER_TYPE;
    }

    /**
     * Sets the converter attribute value.
     *
     * @param converter the converter attribute value
     */
    public void setConverter(
            String converter) {
        _converter = converter;
    }

    /**
     * Sets the value attribute value.
     *
     * @param value the value attribute value
     */
    public void setValue(
            String value) {
        _value = value;
    }

    /**
     * Releases the internal state used by the tag.
     */
    public void release() {
        _converter = null;
        _value = null;
    }

    /**
     * Transfers the property values from this tag to the component.
     *
     * @param component the target component
     */
    protected void setProperties(
            UIComponent component) {
        super.setProperties(component);
        // Behavioral properties
        setValueBindingProperty(component, "converter", _converter);
        setStringProperty(component, "value", _value);
        // Renderer-specific attributes (none)
    }

    /**
     * The converter attribute.
     */
    private String _converter;

    /**
     * The value attribute.
     */
    private String _value;
}