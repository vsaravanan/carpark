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



<nav class="navbar navbar-inverse navbar-fixed-top">



  <div class="container-fluid" >




    <div class="navbar-header" >
       <span class="navbar-brand"><spring:message code="page.title"/></span>
    </div>

	<div id="divMenu" >
	    <ul class="nav navbar-nav">
	    	<li><a href="${root}"><spring:message code="label.navigation.home.link"/></a></li>
<%-- 		<sec:authorize access="isAuthenticated()">
	            <li><a href="${root}allusers" >User</a></li>
		</sec:authorize> --%>

		<sec:authorize access="hasRole('ROLE_Internal')">
	            <li><a href="${root}vehicle">Vehicle</a></li>
	            <li><a href="${root}calendar">Calendar</a></li>
	            <li><a href="${root}parkingSlot">ParkingSlot</a></li>
	            <li><a href="${root}slotUsed">Reserve/Park</a></li>
	            <li><a href="${root}parking">View_Parkings</a></li>
	            <li><a href="${root}parkingBill">Billing</a></li>
		</sec:authorize>

		<sec:authorize access="hasRole('ROLE_External')">
	            <li><a href="${root}carpark">Carparks</a></li>
	            <li><a href="${root}external/showUploadCarpark">Upload</a></li>
		</sec:authorize>

	    </ul>
	    <ul class="nav navbar-nav navbar-right">
	    	<li><a href="#"><sitemesh:write property='title'/></a> </li>
	      <sec:authorize access="isAnonymous()">
	      	<li>
	      		<a href="${root}guest/signup"><span class="glyphicon glyphicon-user">
	      		</span><spring:message code="label.navigation.SignUp.link"/>
	      		</a>
	      	</li>
	      	<li><a href="${root}login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
	      </sec:authorize>
	      <sec:authorize access="isAuthenticated()">

	          <li>
	              <form action="${root}logout" method="POST">
	                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	                  <button type="submit" class="btn btn-default navbar-btn">
	                      <spring:message code="label.navigation.logout.link"/>
	                  </button>
	              </form>
	          </li>
	       <p class="navbar-text pull-right">
	           <a href="${root}allusers" class="navbar-link">UserId : ${userId}</a>
	       </p>
	      </sec:authorize>


	    </ul>
	</div>
  </div>
</nav>

    <div id="mainWrapper" class="content">
        <div id="view-holder">
        	<%@include file="utilTemplates.htm" %>
            <sitemesh:write property="body"/>

		            
            
        </div>
    </div>
	<footer>

<script>

$(function() {
       	$("#formMsg").val("");
        $("#formErr").val("");
});
</script>

	  <p>&copy; Saravanan Software Services, 2016</p>
	</footer>
</div>
</body>
</html>