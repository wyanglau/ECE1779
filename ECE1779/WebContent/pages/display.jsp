<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ece1779.commonObjects.User"%>
<%@page import="ece1779.commonObjects.Images"%>
<%@page import="ece1779.Main"%>
<%@page import="ece1779.GlobalValues"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Images Glance</title>

<style type="text/css">
td {
	width: 10%;
	valign: middle;
}

img {
	max-width: 256px;
	max-height: 256px;
	height: auto;
}

.sl-custom-file {
	position: relative;
	display: inline-block;
	zoom: 1;
	cursor: pointer;
	overflow: hidden;
	vertical-align: middle;
}

.ui-input-file {
	position: absolute;
	right: 0;
	top: 0;
	_zoom: 30;
	font-size: 300px\9;
	height: 100%;
	_height: auto;
	opacity: 0;
	filter: alpha(opacity = 0);
	-ms-filter: "alpha(opacity=0)";
	cursor: pointer;
}

.divDash {
	border: 1px dashed #000;
	width: 500px;
	align: center;
}

.exitButton {
	background-color: #44c767;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	border-radius: 4px;
	border: 1px solid #18ab29;
	display: inline-block;
	cursor: pointer;
	color: #ffffff;
	font-family: arial;
	font-size: 17px;
	padding: 2px 14px;
	text-decoration: none;
	text-shadow: 0px 1px 0px #2f6627;
}

.exitButton:hover {
	background-color: #5cbf2a;
}

.exitButton:active {
	position: relative;
	top: 1px;
}

.submitButton {
	background-color: #44c767;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	border-radius: 4px;
	border: 1px solid #18ab29;
	display: inline-block;
	cursor: pointer;
	color: #ffffff;
	font-family: arial;
	font-size: 17px;
	padding: 2px 57px;
	text-decoration: none;
	text-shadow: 0px 1px 0px #2f6627;
}

.submitButton:hover {
	background-color: #5cbf2a;
}

.submitButton:active {
	position: relative;
	top: 1px;
}
</style>
</head>
<body>
	<script type="text/javascript">
		function manualGrow() {
			document.display.action = "../ManualGrowServlet";
			document.display.submit();
		}
	</script>

	<%
		//Main init = (Main) session.getAttribute(GlobalValues.USER_INIT);
		//User user = init.getUser();

		//test data
		User user = new User(1, "ryan", new ArrayList<Images>());
	%>
	<form
		action=<%=response.encodeURL(request.getContextPath()
					+ "/servlets/LogoutServlet")%>
		method=POST>
		<div align=center>
			<font size=20> <b>Welcome, <%=user.getUserName()%>!
			</b> <input class=exitButton type=submit value=exit>
			</font>
		</div>
	</form>
	<br>
	<form
		action=<%=response.encodeURL(request.getContextPath())
					+ "/FileUploadServlet"%>
		enctype="multipart/form-data" method="post">
		<div align=center>
			<div class=divDash>
				<br> Upload a new image?<br> <br> <input type=hidden
					name="userID">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
					type="file" name="theFile" /> <br> <br> <input
					class=submitButton type=submit> <br> <br>
			</div>
		</div>
	</form>
	<br />
	<table>
		<%
			for (Images img : user.getImgs()) {
		%>
		<tr>
			<%
				for (String key : img.getKeys()) {
			%>
			<td><img src="<%=key%>"></td>
			<%
				}
			%>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>