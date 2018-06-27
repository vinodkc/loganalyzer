<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
<title>My Dirty UI :) - Upload File Request Page</title>
</head>
<body>
	<form method="POST" action="uploadFile" enctype="multipart/form-data">
		
		
		Component Name: <input type="text" name="name"><br />
		Case#: <input type="text" name="caseid"><br />
		Upload load file in tar format: <input type="file" name="file"><br />  <br /> 
		Click here! <input type="submit" value="Analyze"> 
	</form>	
</body>
</html>