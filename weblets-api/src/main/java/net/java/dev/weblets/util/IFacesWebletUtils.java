package net.java.dev.weblets.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Werner Punz
 * Date: 30.12.2007
 *
 * Generic contractual interface for the weblet utils
 */
public interface IFacesWebletUtils {
      public String getResource(FacesContext context, String weblet, String pathInfo, boolean fullUrl);
}