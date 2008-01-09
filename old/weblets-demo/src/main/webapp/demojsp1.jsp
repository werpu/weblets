<%-- 
    Document   : demojsp1.jsp
    Created on : 14.12.2007, 16:53:29
    Author     : werpu
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean class="com.apress.projsf.weblets.misc.WebletsJSPBean" scope="application" id="jspwblUrl" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Hello World!</h2>
        <%= jspwblUrl.getResourceUrl("weblet://com.apress.projsf.weblets.demo/welcome.js") %>
        <br /> second way <br />
        <%= jspwblUrl.getResourceUrl("com.apress.projsf.weblets.demo","/welcome.js") %>
 
        
   </body>
</html>
