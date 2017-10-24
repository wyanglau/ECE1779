<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ece1779.GlobalValues"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Welcome to group14 Project!</title>
    <style>
  		table { text-align:center; }
	</style>
</head>
<body>
	<form method="post" action="servlets/LoginServlet">
		<center>
			<table border="0" cellpadding="3">
				<thead>
					<tr>
						<th>Login</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><font size=2>Please Register first if this is your
								first time here</font></td>
					</tr>
					<tr>
						<td><input type="text" name=<%=GlobalValues.USERNAME%>
							value="" placeholder="username" /></td>
					</tr>
					<tr>
						<td><input type="password" name=<%=GlobalValues.PASSWORD%>
							value="" placeholder="password" /></td>
					</tr>
					<tr>
						<td align="center"><input type="submit" value="Login" /></td>
					</tr>
				</tbody>
			</table>
		</center>
	</form>
	<br>
	<form method="post" action="servlets/RegistrationServlet">
		<center>
			<table border="0" cellpadding="3">
				<thead>
					<tr>
						<th>Registration</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><input type="text" name=<%=GlobalValues.regUSERNAME%> value=""
							placeholder="username" /></td>
					</tr>
					<tr>
						<td><input type="password" name=<%=GlobalValues.regPASSWORD%> value=""
							placeholder="password" /></td>
					</tr>
					<tr>
						<td><input type="password" name=<%=GlobalValues.regPASSWORD2%> value=""
							placeholder="retype password" /></td>
					</tr>
					<tr>
						<td align="center"><input type="submit" value="Register" /></td>
					</tr>
				</tbody>
			</table>
		</center>
</body>
</html>