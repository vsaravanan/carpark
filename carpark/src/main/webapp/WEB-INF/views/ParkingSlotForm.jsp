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
<title>Parking Slot edit</title>
<script>
	$(function() {
		document.body.style.zoom="110%";
	});
</script>

</head>
<body>
	<div align="center" class="hero-unit">


		<h2>Parking Slot page</h2>
		<spring:url	 value="/"		var="root"	 />
		<table >
			<form:form action= "${root}parkingSlot/save"   modelAttribute="parkingSlot" method="post" >
			<form:hidden path="slotId"/>

			<thead>
				<tr>
					<td class="col-sm-4 control-label"> </td>
					<td class="col-sm-8 control-label"></td>

				</tr>
			</thead>


			<tr>
				<td>Parking Slot Id: </td>
				<td> <b>${parkingSlot.slotId == null ? "New" : parkingSlot.slotId}</b></td>
			</tr>

			<tr>
				<td>Building name / Location:</td>
				<td><form:input path="location" />
				<form:errors path="location" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Level or Floor or Storey:</td>
				<td><form:input path="level" type="number" />
				<form:errors path="level" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Slot No:</td>
				<td><form:input path="slotNo" type="number" />
				<form:errors path="slotNo" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Tag Id:</td>
				<td><form:input path="tagId" />
				<form:errors path="tagId" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Status:</td>
				<td>
					<form:select path="status" >
						<form:options items="${status}"    />
					</form:select>
					<form:errors path="status" cssClass="error" /></td>
			</tr>

			<tr>
				<td colspan="2" align="center">
					<button type="submit" class="btn btn-primary pull-right">
							${parkingSlot.slotId == null ? "Add" : "Update"}</button>

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