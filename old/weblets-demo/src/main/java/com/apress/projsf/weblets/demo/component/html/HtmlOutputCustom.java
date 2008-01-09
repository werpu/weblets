package com.apress.projsf.weblets.demo.component.html;

import javax.faces.component.UIOutput;

/**
 * The HtmlOutputCustom component.
 */
public class HtmlOutputCustom extends UIOutput
{
  /**
   * The component type for this component.
   */
  public static final String COMPONENT_TYPE = "com.apress.projsf.weblets.demo.HtmlOutputCustom";

  /**
   * The renderer type for this component.
   */
  public static final String RENDERER_TYPE = "com.apress.projsf.weblets.demo.Custom";

  /**
   * Creates a new HtmlOutputCustom.
   */
  public HtmlOutputCustom()
  {
    setRendererType(RENDERER_TYPE);
  }
}
