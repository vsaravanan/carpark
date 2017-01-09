<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
		<title>Login page</title>

<script>
$(function() {

var jqxhr = $.ajax( "http://www.google.com" )
.done(function() {
  console.log( "success" );
})
.fail(function() {
	console.log( "error" );
})
.always(function() {
	console.log( "complete" );
});

//Perform other work here ...

//Set another completion function for the request above
jqxhr.always(function() {
	console.log( "second complete" );
});


});

</script>

	</head>

	<body>
	testing
	</body>
	</html>