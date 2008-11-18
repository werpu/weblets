<%-- 
    Document   : demojsp1.jsp
    Created on : 14.12.2007, 16:53:29
    Author     : werpu
--%>

<%@page contentType="text/html" pageEncoding="windows-1252" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean class="net.java.dev.weblets.WebletUtils" scope="application" id="jspweblet"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>JSP Page</title>
    <link rel="stylesheet" href="<%=jspweblet.getURL("weblets.demo", "/styles/weblets.css")%>"></link>
</head>
<body>
<div class="header_bg">
    &nbsp;
</div>
<div class="content">

    <p>
        New in Version 1.1 local resources, the images below is streamed
        from a local webapp directory.

        Also possible, the clear definition of allowed filetypes
        to avoid accidental display of unwanted resources.
    </p>

    <%=jspweblet.getURL("weblets.demo", "/welcome.js")%>
    <br/>
    <!-- Local weblet defined here  -->
    <script type="text/javascript" src="<%=jspweblet.getURL("weblets.webapp", "/helloworld.js")%>"></script>
    <img src="<%=jspweblet.getURL("weblets.webapp", "/helloworld.png")%>"/>
    the image can be found locally under resources/helloworld.png!!!
    <a href="<%=jspweblet.getURL("weblets.source", "/localresource.jsp")%>" target="_new">[Get the page source]</a>
    <br/>
    <h:outputLink value='#{weblet.url[ "weblets.source"]["/localresource.jsp"]}' target="_new"><h:outputFormat
            value="[Get the page source]"/></h:outputLink>

</div>
</body>
</html>
