<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
	<head>
		<title>Login page</title>
<style>

button{
  margin: 10px;
}


</style>

<script>
$(function() {

	
/*     $("#descrtrtribeMe").click(function(){
    	var strWindowFeatures = "location=yes,height=360,width=640,scrollbars=yes,status=yes";
    	var URL = "https://www.youtube.com/embed/24XRDBn-0XE?&cc_load_policy=1&autoplay=1&rel=0";
    	var win = window.open(URL, "_blank", strWindowFeatures);
    }); */

/*     $("#describeMe").click(function (e) {
        //event.preventDefault();

        var thisBtn = $(this);

        var url =  thisBtn.data("alt"); 
      
        
        //if (isValidURL(url) == false)
        //	url =  thisBtn.data("alt"); 
        
   

        //url =  url + "?&cc_load_policy=1&autoplay=1&rel=0";
        var windowName = "_blank";
        var windowSize = "location=yes, width=640, height=360, scrollbars=yes,status=yes";

        window.open(url, windowName, windowSize);
    });    */ 

});
</script>


	</head>

	<body>
	<div align="center" class="hero-unit">
		<div id="mainWrapper2">
			<c:url var="loginUrl" value="${root}login" />
			<form action="${loginUrl}" method="post" class="form-horizontal">		

	  			<div class="container-fluid" >
					<div class="row-fluid" >
						<div class="login-container">
							<div class="login-card">
								<div class="login-form">
			
									<c:if test="${param.error != null}">
										<div class="alert alert-danger">
											<p>Invalid username and password. ${param.error}</p>
										</div>
									</c:if>
									<c:if test="${param.logout != null}">
										<div class="alert alert-success">
											<p>You have been logged out successfully.</p>
										</div>
									</c:if>
									<div class="input-group input-sm">
										<label class="input-group-addon" for="userName"><i class="fa fa-user"></i></label>
										<input type="text" class="form-control" id="userName" name="userName" placeholder="Enter Username" required>
									</div>
									<div class="input-group input-sm">
										<label class="input-group-addon" for="pwd"><i class="fa fa-lock"></i></label>
										<input type="password" class="form-control" id="pwd" name="pwd" placeholder="Enter Password" required>
									</div>
			
									<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
									<br>
									<div class="form-actions">
										<input type="submit"
											class="btn btn-block btn-primary btn-default" value="Log in">
									</div>
								</div>	
							</div>
						</div>	
					</div>
					<br>

					<sec:authorize access="permitAll">
						<div class="row-fluid " >
							<div class="span4" > </div>
							<div class="span4">

	  							<div class="container-fluid" align="left">
									<div class="row-fluid" >
										Demo Internal User : raman/pwd <br>
										Demo External User : anbu/pwd <br>
										Demo Admin User : admin/pwd <br><br>
									</div>

									<div  class="row-fluid">

										<div class="span8">Install Android demo app into your mobile to test carpark reservation </div>
										<div class="span4" style='height: 72px; width: 72px;'>
											<a href="file/SmartParkingApp.apk"> 
											<img src="img/smartcarpark.png" class="img-rounded" 
											style='height: 50%; width: 50%; object-fit: contain'>
											</a>
											<br>
										</div>	
									</div>							
									<div  class="row-fluid" > 									

										<button id="describeMe" type="button" class="btn btn-warning js-newWindow" 
											data-href="https://www.youtube.com/embed/24XRDBn-0XE" 
											data-alt="download/showVideo/CarparkWebApplication1LoginPage.mp4"
											>Read Me</button>

										<button type="button" class="btn btn-warning"
										onclick="location.href='http://<%= request.getServerName() %>:7070' "
										>View spring tutorials</button>
									</div>									


								</div>
							</div>
							<div class="span4" > </div>


						</div>
					</sec:authorize>	
				</div>
			</form>
		</div>
	</div>

	</body>
</html>