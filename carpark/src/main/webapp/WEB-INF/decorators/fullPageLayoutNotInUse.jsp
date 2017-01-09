<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
   <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
    	<meta name="author" content="Saravanan, Saravanan Software services">

		<spring:url	 value="/"		var="root"	 />
		<sec:authorize var="isLogin" access="isAuthenticated()" />

		<title><sitemesh:write property='title'/> [ Carpark WebApp ]</title>


        <link href="${root}css/bootstrap.css" rel="stylesheet" >
        <link href="${root}css/bootstrap-responsive.css" rel="stylesheet" />
		<link href="${root}css/font-awesome.css" rel="stylesheet" />

		<script src="${root}js/jquery.js" ></script>
    	<script src="${root}js/jszip.min.js" ></script>
		<script src="${root}js/bootstrap.js" ></script>

		<link href="${root}/styles/kendo.common.min.css" rel="stylesheet" />
		<link href="${root}/styles/kendo.rtl.css" rel="stylesheet" />
		<link href="${root}/styles/kendo.moonlight.css" rel="stylesheet" />
		<link href="${root}/styles/kendo.dataviz.min.css" rel="stylesheet" />
		<link href="${root}/styles/kendo.dataviz.default.min.css" rel="stylesheet" />
		<link href="${root}/css/util.css" rel="stylesheet" />


	<script src="${root}js/kendo.web.min.js"></script>
    <script src="${root}js/kendo.all.min.js"></script>
    <script src="${root}js/console.js"></script>
    <script src="${root}js/util.js"></script>


    <sitemesh:write property="head"/>


    </head>

<body>


<div class="page">



    <div id="mainWrapper" class="content">
        <div id="view-holder">
        	<%@include file="utilTemplates.htm" %>
            <sitemesh:write property="body"/>

        </div>
    </div>
	<footer>

	  <p>&copy; Saravanan Software Services, 2016</p>
	</footer>
</div>
</body>
</html>