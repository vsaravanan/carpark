<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
		<%@include file="csrf.jsp" %>
<title>Parking Bill</title>
<script>
$(function() {
	document.body.style.zoom="100%";

	$(".datetimepickerFrom").datetimepicker();
	$('#exitTimeInReserve').datetimepicker();

});
</script>
</head>
<body>
	<div align="center" class="hero-unit">


		<h2>Parking Bill page</h2>
		<spring:url	 value="/"		var="root"	 />
		<table>
			<form:form action= "${root}parkingBill/save"   modelAttribute="parkingBill" method="post" >
			<form:hidden path="billId"/>

			<thead>
				<tr>
					<td class="col-sm-5 control-label"> </td>
					<td class="col-sm-7 control-label" align="center"></td>
				</tr>
			</thead>
			<tr>
				<td>Parking Bill Id:</td>
				<td><b>${parkingBill.billId == null ? "New" : parkingBill.billId}</b></td>
			</tr>
			<tr>
				<td>Reserve In (expected entry time):</td>
				<td>
				<form:input path="entryTimeInReserve"  cssClass="date-picker" />
				<form:errors path="entryTimeInReserve" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Reserve Out (expected exit time):</td>
				<td>
				<form:input path="exitTimeInReserve" />
				<form:errors path="exitTimeInReserve" cssClass="error" />
				</td>
			<tr>
				<td>Parking in (actual entry time):</td>
				<td><form:input path="entryTime" />
				<form:errors path="entryTime" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Parking out (actual exit time):</td>
				<td><form:input path="exitTime" />
				<form:errors path="exitTime" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Number of minutes parked:</td>
				<td><form:input path="numMinsParked" type="number" />
				<form:errors path="numMinsParked" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Parking Charges:</td>
				<td><form:input path="charges" type="number" />
				<form:errors path="charges" cssClass="error" />
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
							${parkingBill.billId == null ? "Add" : "Update"}</button>

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