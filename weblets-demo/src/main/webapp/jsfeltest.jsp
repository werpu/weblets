<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:loadBundle basename="jsfks.bundle.messages" var="msg"/>

<html>
 <head>
  <title>enter your name page</title>
 </head>
 <body>
   <f:view>
      <h:outputText value="#{jsfwblUrl['weblet://com.apress.projsf.weblets.demo/welcome.js']}" />
   </f:view>
 </body>
</html> 