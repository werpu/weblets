package net.java.dev.weblets;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * a simple servlet filter
 * to enable webapp context processing
 * from non jsf and non jee5 systems
 *
 */
public class WebletsContextFilter implements Filter {
    WebletContainer wblContainer = null;
    boolean isSetup = false;
    public void init(FilterConfig filterConfig) throws ServletException {
       wblContainer = WebletContainer.getInstance();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(!isSetup) {
            synchronized(wblContainer) {
                isSetup = true;
                if(wblContainer.getWebletContextPath() == null || wblContainer.getWebletContextPath().trim().equals("")) {
                      wblContainer.setWebletContextPath(((HttpServletRequest)servletRequest).getContextPath());
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }
}
