<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

<html>
    <head>
		<%@include file="csrf.jsp" %>
        <title>Users List</title>
    </head>
    <body>

        <div id="mainWrapper2" align="center">
	        <h1>Users List</h1>
	        <h3><a href="add">Add</a></h3>

        	<table border="1">
        	<thead>
	        	<tr>
		        	<th>No</th>
		        	<th>User Name</th>
		        	<th>Address</th>
		        	<th>Email</th>
		        	<th>Phone No</th>
		        	<th>Vehicle</th>
		        	<th>User Type</th>
		        	<th>Actions</th>
		        </tr>
	        </thead>
				<c:forEach var="user" items="${users}" >
	        	<tr>
					<td>${user.userId}</td>
					<td>${user.userName}</td>
					<td>${user.address}</td>
					<td>${user.email}</td>
					<td>${user.phoneNo}</td>
					<td>${user.vehicleId}</td>
					<td>${user.userType}</td>
					<td>
						<button class="btn btn-primary" onclick="location.href='edit/${user.userId}'">Edit</button>
						<button class="btn btn-danger"  data-target="#myModal_${user.userId}"  data-toggle="modal" data-id="${user.userId}">Delete</button>

					</td>

	        	</tr>
					<div id="myModal_${user.userId}" class="modal fade">
					    <div class="modal-dialog">
					        <div class="modal-content">
					            <div class="modal-header">
					                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					                <h4 class="modal-title">Confirm Delete</h4>
					            </div>

					            <div class="modal-body">
					                <p>Are you sure you want to delete this user Id ${user.userId}? </p>
					            </div>
					            <div class="modal-footer">

					                <button type="button" class="btn btn-cancel" data-dismiss="modal">Close</button>
					                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="location.href='delete/${user.userId}'" >Delete</button>

					            </div>
					        </div>
					    </div>
					</div>
				</c:forEach>
				<tr>
					<td colspan="2" align="center">
						 ${message}
					</td>
				</tr>
        	</table>
        </div>
    </body>
</html>
