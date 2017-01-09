<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>Tutorial page</title>
	<spring:url	 value="/"		var="root"	 />	

	
    <link href="${root}css/bootstrap.css" rel="stylesheet" >	
    <link href="${root}css/ShowVideo.css" rel="stylesheet" >	

	<script src="${root}js/jquery.js" ></script>
	<script src="${root}js/bootstrap.js" ></script> 
	<script src="${root}js/ShowVideo.js" ></script> 



</head>

<body>

    <div id="mainWrapper3"  >
	    <video id="video1" width="640" height="267"  controls="controls"  autoplay="autoplay">
	        <source src="${root}file/${videoName}.mp4" type="video/mp4" >
 	 		<track label="English" kind="subtitles" srclang="en"
 	 			 src="${root}file/${videoName}.srt" default >
			<p>Your browser does not support the HTML5 video</p>  
			       
	    </video>
	    <br>
		<div align="center" >
		

		  <button type="button"  class="btn btn-primary btn-xs " onclick="btnPlay()">
		  	<span class="glyphicon glyphicon-play"></span>  Play
		  </button>
		  <button type="button"  class="btn btn-primary btn-xs " onclick="btnPause()">
		  	<span class="glyphicon glyphicon-pause"></span>  Pause
		  </button>
		  <button type="button"  class="btn btn-primary btn-xs " onclick="btnIncrease()">
		  	<span class="glyphicon glyphicon-volume-up"></span>  Vol+
		  </button>
		  <button type="button"  class="btn btn-primary btn-xs " onclick="btnDecrease()">
		  	<span class="glyphicon glyphicon-volume-down"></span>  Vol-
		  </button>
		  <button type="button"  class="btn btn-primary btn-xs " onclick="btnFullScreen()">
		  	<span class="glyphicon glyphicon-fullscreen"></span>  Full screen
		  </button>
		  
			<!-- 		
			<div class="progressTime">
			    Current play time: <span class="current"></span><br>
			    Video duration: <span class="duration"></span>
			</div> -->
	
		</div> 
		<br>		
		<div class="progressBar">
		    <div class="timeBar"></div>
		</div>
    </div>
              
</body>

</html>

