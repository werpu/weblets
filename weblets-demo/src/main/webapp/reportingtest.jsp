<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean class="net.java.dev.weblets.WebletUtils" scope="application" id="jspweblet" />
<jsp:useBean class="net.java.dev.weblets.demo.reporting.ReportingBean" scope="application" id="reportingbean" />

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


            <%=reportingbean.getReportingString()%>

		</div>
	</body>
</html>
