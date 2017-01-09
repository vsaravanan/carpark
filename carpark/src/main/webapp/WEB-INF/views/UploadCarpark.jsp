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
<title>Upload Carpark</title>
<script>


$(function() {
	document.body.style.zoom="110%";

});
</script>
 		<c:url var="actionUrl" value="/external/uploadCarpark?${_csrf.parameterName}=${_csrf.token}"/>

</head>
<body >

	<div  align="center" class="hero-unit">


		<h2>Upload carpark data to system</h2>
		<table>




			<form:form action= "${actionUrl}"   modelAttribute="fileBean" method="post" enctype="multipart/form-data" >

			<thead>
				<tr>
					<td class="col-sm-2 control-label"> </td>
					<td class="col-sm-10 control-label" align="center"></td>

				</tr>
			</thead>

			<tr>
				<td><form:label for="fileData" path="fileData"></form:label></td>
				<td><b><form:input path="fileData" type="file" cssClass="error"/></b></td>
			</tr>


			<tr>
				<td colspan="2" align="center">
					<button type="submit" class="btn btn-primary pull-right">
							Upload</button>
				</td>
			</tr>
			<tr >
				<td colspan="2"  align="center">
				&nbsp;
				</td>
			</tr>

			<c:if test="${error != null}">
				<tr>
					<td colspan="2" align="center">
						<div class="alert alert-danger">
							${fileName} is not a valid excel file <br> Expecting xls or
							xlsx file <br>
						</div>
					</td>
				</tr>
			</c:if>
			<c:if test="${success != null}">
				<tr>
					<td colspan="2" align="center">
						<div class="alert alert-success">
							<p>
							Successfully uploaded file : ${filename}  <br>
							Total records found in excel file : ${totalRecords} <br>
							Valid records inserted into carpark system : ${inserted} <br>
							</p>
						</div>
					</td>
				</tr>
			</c:if>


			</form:form>
		</table>
	</div>

</body>
</html>