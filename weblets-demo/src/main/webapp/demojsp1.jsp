<%-- 
    Document   : demojsp1.jsp
    Created on : 14.12.2007, 16:53:29
    Author     : werpu
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean class="net.java.dev.weblets.WebletsUtils" scope="application" id="jspweblet" />
<%@ taglib uri="http://weblets.dev.java.net/tags" prefix="weblets" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    <body>
        <%= ((HttpServletRequest)pageContext.getRequest()).getContextPath() %>
        <p>
        the getResource results are relative to the context
        so that you can map it to any url you want
        / !!!
        </p>
        <%= jspweblet.getResource( "weblets.demo","/welcome.js") %>



         <p>
        the getUrl results are absolute to the context,
        note this only works either if you are in a jee5 environment
        or if you set the weblets filter,
        alternatively as final override you can set a context-param
        (examples can be found in the web.xml)
        </p>

        <%= jspweblet.getUrl( "weblets.demo","/welcome.js") %>
        <br />          
        <img src="<%= jspweblet.getUrl( "weblets.demo","/img/icon_alert.gif") %>" />
        same with a tag
        <img src='.<weblets:resource weblet="weblets.demo" pathInfo="/img/icon_alert.gif" />' />
   </body>
</html>
