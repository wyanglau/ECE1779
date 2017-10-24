<%@page import="ece1779.GlobalValues"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="ece1779.commonObjects.Images"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ece1779.GlobalValues"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Transformed Images</title>

<style type=text/css>
.divDashImageBorder {
	border: 1px dashed #000;
}

.backbutton {
	background-color: #44c767;
	-moz-border-radius: 12px;
	-webkit-border-radius: 12px;
	border-radius: 12px;
	border: 1px solid #18ab29;
	display: inline-block;
	cursor: pointer;
	color: #ffffff;
	font-family: arial;
	font-size: 20px;
	font-weight: bold;
	padding: 15px 76px;
	text-decoration: none;
	text-shadow: 0px 1px 0px #2f6627;
}

.backbutton:hover {
	background-color: #5cbf2a;
}

.backbutton:active {
	position: relative;
	top: 1px;
}
</style>
</head>
<body>
	<%
		String key1 = request.getParameter("key1").toString();
		String key2 = request.getParameter("key2").toString();
		String key3 = request.getParameter("key3").toString();
		String key4 = request.getParameter("key4").toString();
	%>
	<br>
	<br>
	<br>
	<div align=center>
		<a href="display.jsp" class="backbutton"> ⬅ GO BACK</a>
	</div>
	<br>
	<table align=center>

		<tr>
			<td class=divDashImageBorder><h1>Original</h1> <a
				href="<%=GlobalValues.BUCKET_ENDPOINT + key1%>" target="view_blank">
					<img src="<%=GlobalValues.BUCKET_ENDPOINT + key1%>" width=260>
			</a></td>
			<td class=divDashImageBorder><h1>Flop</h1> <img
				src="<%=GlobalValues.BUCKET_ENDPOINT + key2%>" width=260></td>
		</tr>
		<tr>
			<td class=divDashImageBorder><h1>Monochrome</h1> <img
				src="<%=GlobalValues.BUCKET_ENDPOINT + key3%>" width=260></td>
			<td class=divDashImageBorder><h1>Negate</h1> <img
				src="<%=GlobalValues.BUCKET_ENDPOINT + key4%>" width=260></td>
		</tr>
	</table>
</body>
</html>