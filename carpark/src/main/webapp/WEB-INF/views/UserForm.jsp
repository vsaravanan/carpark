<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
		<%@include file="csrf.jsp" %>
<title>User Page</title>
<script>


$(function() {
//	document.body.style.zoom="110%";

	var userId = $("#userId").val();
	if (!userId)
		$("#pwd").prop('required','required');

});
</script>
</head>
<body >

	<div  align="center" class="hero-unit">


		<h2>User page</h2>
		<spring:url	 value="/"		var="root"	 />
		<table>

			<c:set value="${root}${user.guest ?'guest':'allusers'}/save" var="userUrl"/>

			<c:set value="${user.view ? '#' : userUrl}" var="userUrl"/>

			<form:form action="${userUrl}"   modelAttribute="user" method="post" >
			<form:hidden path="guest"/>
			<form:hidden path="userId"/>

			<thead>
				<tr>
					<td class="col-sm-2 control-label"> </td>
					<td class="col-sm-10 control-label" align="center"></td>

				</tr>
			</thead>

			<tr>
				<td>User id: </td>
				<td><b>${user.userId == null ? "New" : user.userId}</b></td>
			</tr>

			<tr>
				<td>User name:</td>
				<td><form:input path="userName" />
				<form:errors path="userName" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Password:</td>
				<td>
					<form:password path="pwd"  title="Password is mandatory for new user" />
					<form:errors path="pwd" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>Address:</td>
				<td><form:textarea path="address"  rows="2" cols="40" />
				<br>
				<form:errors path="address" cssClass="error" /></td>
			</tr>
			<tr>
				<td>Email:</td>
				<td><form:input path="email"/>
				<br><form:errors path="email" cssClass="error"/></td>
			</tr>
			<tr>
				<td>Phone no:</td>
				<td><form:input path="phoneNo"/>
				<br><form:errors path="phoneNo" cssClass="error" /></td>
			</tr>
			<tr>
				<td>User type:</td>
				<td>
					<form:select path="userType" >
						<form:options items="${userType}"  />
					</form:select>
					<form:errors path="userType" cssClass="error" /></td>
			</tr>
			<tr>
				<td>Vehicle Id:</td>
				<td>
					<form:select path="vehicleId" >
						<form:options items="${vehicleLkp}" />
					</form:select>

				<form:errors path="vehicleId" cssClass="error"/></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<c:if test="${! user.view}">
					<button type="submit" class="btn btn-primary pull-right">
							${user.userId == null ? "Add" : "Update"}</button>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					 ${message}
				</td>
			</tr>

			</form:form>
		</table>
	</div>

</body>
</html>