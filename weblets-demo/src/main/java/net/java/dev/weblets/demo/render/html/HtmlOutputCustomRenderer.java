package net.java.dev.weblets.demo.render.html;

import net.java.dev.weblets.FacesWebletUtils;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;
import java.io.IOException;

public class HtmlOutputCustomRenderer extends Renderer
{
  public void encodeEnd(
    FacesContext context,
    UIComponent  component) throws IOException
  {
    //ViewHandler handler = context.getApplication().getViewHandler();
    String resourceURL = FacesWebletUtils.getURL(context, "weblets.demo", "/welcome.js");
       
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