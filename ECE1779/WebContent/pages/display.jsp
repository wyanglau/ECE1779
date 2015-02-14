<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ece1779.commonObjects.User"%>
<%@page import="ece1779.commonObjects.Images"%>
<%@page import="ece1779.Main"%>
<%@page import="ece1779.GlobalValues"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Images Glance</title>
<style>
td {
	width: 10%;
	valign: middle;
}

img {
	max-width: 256px;
	max-height: 256px;
	height: auto;
}
</style>
</head>
<body>
	<%
		Main init = (Main) session.getAttribute(GlobalValues.USER_INIT);
		User user = init.getUser();
	%>
	<h1>
		Welcome,
		<%=user.getUserName()%>!
	</h1>
	<form
		action=<%=response.encodeURL(request.getContextPath()
					+ "/servlets/LogoutServlet")%>
		method=POST>
		<input type=submit value=exit>
	</form>
	<br />
	<table>
		<%
			for (Images img : user.getImgs()) {
		%>
		<tr>
			<td><img src="<%=img.getOriginal()%>"></td>
			<td><img src="<%=img.getTransFirst()%>"></td>
			<td><img src="<%=img.getTransSecond()%>"></td>
			<td><img src="<%=img.getTransThird()%>"></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>