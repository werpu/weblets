<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:loadBundle basename="jsfks.bundle.messages" var="msg"/>

<html>
 <head>
  <title>enter your name page</title>
 </head>
 <body>
   <f:view>
       test for resource
      <h:outputText value="#{weblet.resource['weblets.demo']['/welcome.js']}" />
       test for an empty resource
       <h:outputText value="#{weblet.resource['weblets.demo']['']}" />
      <p>
       test for url
      <h:outputText value="#{weblet.url['weblets.demo']['/welcome.js']}" />
      </p>
       
      <h:graphicImage id="yyy" value="#{weblet.resource['weblets.demo']['/img/icon_alert.gif']}" />
   
   	  <h:outputLink value='#{weblet.url[ "weblets.source"]["/jsfeltest.jsp"]}' target="_new"><h:outputFormat value="[Get the page source]" /></h:outputLink>
     
   </f:view>
 </body>
</html> 