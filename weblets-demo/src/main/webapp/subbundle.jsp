<%@page contentType="text/html" pageEncoding="windows-1252"%>
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


    <div class="content">
        <p>
            <b>Here we use</b>subbundles to
            concatenate two javascript resources into a single
            request.
            Subbundles can be used to trim down pages to a smaller number
            of requests!
        </p>


        <script type="text/javascript" defer="true"
                src="<%=jspweblet.getURL(request, "weblets.demo","/subbundle/script1.js",true)%>">
        </script>


        <script type="text/javascript" defer="true"
                src="<%=jspweblet.getURL(request, "weblets.demo","/subbundle/script2.js",true)%>">
        </script>

        <p>if you check the page source the second include should be empty while we still reference
            it from our indirection call!
            We use in this case double include checks to suppress additionall calls!
        </p>

        <p>
            alternatively we also simply could use the bundle id without additional calls!
        </p>

        <a href="<%=jspweblet.getURL("weblets.source", "/subbundle.jsp")%>" target="_new">[Get the page source]</a>


    </div>
</div>
</body>
</html>
