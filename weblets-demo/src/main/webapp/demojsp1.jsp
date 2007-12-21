<%-- 
    Document   : demojsp1.jsp
    Created on : 14.12.2007, 16:53:29
    Author     : werpu
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean class="com.apress.projsf.weblets.jsp.WebletsJSPBean" scope="application" id="jspweblet" />
<%@ taglib uri="http://weblets.dev.java.net/tags" prefix="weblets" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    <body>
        <%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>
        <p>
        the urls give are relative to the context so you have to determine the context path yourself 
        unless a framework or lib does it for you, also dont forget the leading
        / !!!
        </p>
        <%= jspweblet.getResource( "com.apress.projsf.weblets.demo","/welcome.js") %>
        <br />
        <img src="./<%= jspweblet.getResource( "com.apress.projsf.weblets.demo","/img/icon_alert.gif") %>" />
        same with a tag
        <img src='./<weblets:resource weblet="com.apress.projsf.weblets.demo" pathInfo="/img/icon_alert.gif" />' />


   </body>
</html>
