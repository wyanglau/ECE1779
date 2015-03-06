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

.divDashImageBorder {
	border: 1px solid #000;
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
		List<String> keys = new ArrayList<String>();
		keys.add("1001_0de77df0-1ae3-415d-919c-5c1ce17131b2");
		keys.add("de8b9ea0-d770-4da3-afba-f13be11fbbad");
		keys.add("2a59ed43-8c90-4dee-bd98-ed7f9fae2801");
		keys.add("f4f99a54-4f89-4191-8ba9-c936dc359d2d");

		List<String> key2 = new ArrayList<String>();
		key2.add("560_350_cat.gif");
		key2.add("560_350_cat.gif");
		key2.add("560_350_cat.gif");
		key2.add("560_350_cat.gif");
		Images imgobj = new Images(user.getId(), 1, key2);
		Images imgobj2 = new Images(user.getId(), 2, keys);
		Images imgobj3 = new Images(user.getId(), 3, key2);
		Images imgobj4 = new Images(user.getId(), 4, keys);
		Images imgobj5 = new Images(user.getId(), 5, key2);
		Images imgobj6 = new Images(user.getId(), 6, keys);
		Images imgobj7 = new Images(user.getId(), 7, key2);
		user.addImage(imgobj);
		user.addImage(imgobj2);
		user.addImage(imgobj3);
		user.addImage(imgobj4);
		user.addImage(imgobj5);
		user.addImage(imgobj6);
		user.addImage(imgobj7);
		//test data
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
			List<Images> images = user.getImgs();
			for (int i = 0; i < images.size(); i += 4) {
		%>
		<tr>
			<%
				for (int j = i; (j < i + 4) && (j < images.size()); j++) {
						List<String> imagekeys = images.get(j).getKeys();
						String key = imagekeys.get(0);
						String href = "imageDetail.jsp?key1=" + key + "&key2="
								+ imagekeys.get(1) + "&key3=" + imagekeys.get(2)
								+ "&key4=" + imagekeys.get(3);
			%>
			<td align=center><a href=<%=href%>><img
					src="<%=GlobalValues.BUCKET_ENDPOINT + key%>" width=auto
					onclick="setImage()"></a></td>
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