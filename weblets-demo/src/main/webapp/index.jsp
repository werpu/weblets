<!-- 
    Document   : index.html
    Created on : 14.12.2007, 17:16:52
    Author     : werpu
-->
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean class="net.java.dev.weblets.WebletUtils" scope="application" id="jspweblet" />

<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<link rel="stylesheet" href="<%=jspweblet.getURL("weblets.demo", "/styles/weblets.css")%>"></link>
	</head>
	<body>
		<div class="header_bg">
			&nbsp;
		</div>
		<div class="content">
			<h1>
				Welcome to the Weblets demo
			</h1>
			<h2>
				You can see in various frameworks on how to use weblets
			</h2>
			<p>
				click on one of the links below
			</p>
			<ul>
				<li>
					<a href="./demo.html">Weblets Simple demo testcase a simple include via mapped urls </a>
				</li>
				<li>
					<a href="./demojsf.html">Weblets Simple demo jsf testcase a simple include via mapped jsf servlet urls </a>
				</li>
				<li>
					<a href="./demojsp1.jsp">A simple weblet include via a jsp bean</a>
				</li>
				<li>
					<a href="./demojsp2.jspx">A simple weblet include via an el function</a>
				</li>
				<li>
					<a href="./jsfeltest.jsf">A simple weblet include via a jsf managed bean map</a>
				</li>
				<li>
					<a href="./faces/outputCustom.jspx">A simple demo of a jsf control utilizing weblets</a>
				</li>
				<li>
					<a href="./jspdojotest.jsp">A big demo utilizing weblets and the dojo toolkit</a>
				</li>
                <li>
                    <a href="./localresource.jsp">A demo utilizing the webapp weblet to serve resources from the local web application</a>
                </li>
                <li>
                    <a href="./subbundle.jsp">Subbundles</a>
                </li>

			</ul>
		</div>
	</body>
</html>
