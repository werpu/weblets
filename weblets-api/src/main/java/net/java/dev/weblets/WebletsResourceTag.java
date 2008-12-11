package net.java.dev.weblets;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Weblets Tag for misc
 * 
 * @author Werner Punz werner.punz@gmail.com
 */
public class WebletsResourceTag extends TagSupport {
	String	weblet		= "";
	String	pathInfo	= "";

	public WebletsResourceTag() {
	}

	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	public int doEndTag() throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(WebletUtils.getResource(weblet, pathInfo));
		} catch (IOException e) {
			e.printStackTrace();
			throw new JspException(e);
		}
		return super.doEndTag();
	}

	public String getWeblet() {
		return weblet;
	}

	public void setWeblet(String weblet) {
		this.weblet = weblet;
	}

	public String getPathInfo() {
		return pathInfo;
	}

	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}
}
