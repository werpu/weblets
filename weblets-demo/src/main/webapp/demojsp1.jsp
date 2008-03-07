<%-- 
    Document   : demojsp1.jsp
    Created on : 14.12.2007, 16:53:29
    Author     : werpu
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean class="net.java.dev.weblets.WebletUtils" scope="application" id="jspweblet" />

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
			<%=((HttpServletRequest) pageContext.getRequest()).getContextPath()%>
			<p>
				the getResource results are relative to the context so that you can map it to any url you want / !!!
			</p>
			<%=jspweblet.getResource("weblets.demo", "/welcome.js")%>



			<p>
				the getURL results are absolute to the context, note this only works either if you are in a jee5 environment or if you set the weblets filter, alternatively as final override you can set a
				context-param (examples can be found in the web.xml)
			</p>

			<%=jspweblet.getURL("weblets.demo", "/welcome.js")%>
			<br />
			<img src="<%=jspweblet.getURL("weblets.demo", "/img/icon_alert.gif")%>" />

			<a href="<%=jspweblet.getURL("weblets.source", "/demojsp1.jsp")%>" target="_new">[Get the page source]</a>
		</div>
	</body>
</html>
