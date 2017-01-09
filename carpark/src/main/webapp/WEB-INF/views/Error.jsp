<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>


<title>Error page</title>
 <script type="text/javascript">
 $(document).ready(function() {

	var errorSource = $("#errorSource").text();
	if (errorSource!='')
	{
	 	$("#"+errorSource).css('visibility','visible');
		 $("#stackTrace").remove();
	}
	else
		 $("#customised").remove();



 })

 </script>

</head>
<body>
    <h4> Failed URL: ${url} </h4>
	<div id="customised">
	    <div id="errorSource" style="visibility: hidden;">${errorSource}</div>

	    <div id="childRecordExists" style="visibility: hidden;color:red;" >
			<h3>
			    Child record exists for ${keyIdAndValue} <br>
				Check the child pages ${childPages}. <br>
				Delete them first <br><br>
			</h3>

	    </div>

	    <div id="IdAlreadyExists" style="visibility: hidden;color:red;" >
			<h3>
				${entityId} Id ${keyId} already exists.  <br>
				Create a new ${entityId} Id.  <br>
				It should be unique <br><br>
			</h3>

	    </div>

	    <div id="InvalidExcelFile" style="visibility: hidden;color:red;" >
			<h3>
				${fileName} is not a valid excel file  <br>
				Expecting xls or xlsx file  <br>
			</h3>

	    </div>
    </div>

    <h4> Exception:  ${exception.message} </h4>

    <div id="stackTrace">
        <c:forEach items="${exception.stackTrace}" var="ste">
        	 ${ste}
    	</c:forEach>
    </div>
</body>
</html>


