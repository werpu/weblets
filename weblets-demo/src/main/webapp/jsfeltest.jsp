<%@page contentType="text/html" pageEncoding="windows-1252" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:loadBundle basename="jsfks.bundle.messages" var="msg"/>
<jsp:useBean class="net.java.dev.weblets.WebletUtils" scope="application" id="jspweblet"/>
<!-- note this usebean is only for the header css includes
in facelets or in jsf 1.2 you can work over the entire page with el functions -->
<html>
<head>
    <title>enter your name page</title>
    <link rel="stylesheet" href="<%=jspweblet.getURL("weblets.demo", "/styles/weblets.css")%>"></link>
</head>
<body>
<f:view>
    <f:verbatim>
        <div class="header_bg"/>
    </f:verbatim>

    <f:verbatim>
        <div class="content">

    </f:verbatim>

    test for resource
    <h:outputText value="#{weblet.resource['weblets.demo']['/welcome.js']}"/>
    test for an empty resource
    <h:outputText value="#{weblet.resource['weblets.demo']['']}"/>
    <p>
        test for url
        <h:outputText value="#{weblet.url['weblets.demo']['/welcome.js']}"/>
    </p>

    <h:graphicImage id="yyy" value="#{weblet.resource['weblets.demo']['/img/icon_alert.gif']}"/>

    <h:outputLink value='#{weblet.url[ "weblets.source"]["/jsfeltest.jsp"]}' target="_new"><h:outputFormat
            value="[Get the page source]"/></h:outputLink>
    <f:verbatim>
        </div>
    </f:verbatim>
</f:view>
</body>
</html> 