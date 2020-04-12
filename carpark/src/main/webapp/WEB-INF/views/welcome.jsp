
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Welcome page</title>
	<spring:url	 value="/"		var="root"	 />		
	<spring:url	 value="https://saravan-js.com:10003"		var="carparkVideos"	 />
</head>
<body>

		<div id="mainWrapper2" style="color: green; font-size: x-large;" align="center"   >
			Dear ${userId}, Welcome to Saravanan Software Services carpark application.
			<p>
			
					<button id="describeMe" type="button" class="btn btn-warning js-newWindow" 
						data-href="https://www.youtube.com/embed/-fkJBQu4ImI" 
						data-alt="/download/showVideo/CarparkWebApplication1LoginPage"
						>Read me on Spring Security</button>
			<p>														
				<a href="<c:url value="/login?logout" />">Logout</a>
		</div>
</body>
</html>