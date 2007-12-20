package com.apress.projsf.weblets.demo.render.html;

import com.apress.projsf.weblets.faces.JSFWebletUtils;
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;

public class HtmlOutputCustomRenderer extends Renderer
{
  public void encodeEnd(
    FacesContext context,
    UIComponent  component) throws IOException
  {
    //ViewHandler handler = context.getApplication().getViewHandler();
    String resourceURL = JSFWebletUtils.getResource(context, "com.apress.projsf.weblets.demo", "welcome.js", component);
       
    UIOutput output = (UIOutput)component;
    Object value = output.getValue();
    String stringValue = null;

    if (value != null)
    {
      Converter converter = output.getConverter();
      if (converter != null)
        stringValue = converter.getAsString(context, output, value);
      else
        stringValue = value.toString();
    }

    ResponseWriter out = context.getResponseWriter();
    out.startElement("script", component);
    out.writeAttribute("type", "text/javascript", null);
    out.writeAttribute("src", resourceURL, null);
    out.endElement("script");

    if (stringValue != null)
    {
      out.startElement("a", component);
      out.writeAttribute("href", "#", null);
      out.writeAttribute("onclick", "sayHello()", null);
      out.writeText(stringValue, null);
      out.endElement("a");
    }
  }

}