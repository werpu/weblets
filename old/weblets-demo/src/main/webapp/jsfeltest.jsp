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
      <h:outputText value="#{wbl_resourceUrl['weblet://com.apress.projsf.weblets.demo/welcome.js']}" />
   </f:view>
 </body>
</html> 