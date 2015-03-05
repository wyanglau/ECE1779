<%@page import="com.amazonaws.services.cloudwatch.model.Datapoint"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ece1779.GlobalValues"%>
<%@page import="ece1779.loadBalance.*"%>
<%@page import="ece1779.commonObjects.*"%>
<%@page import="java.util.List"%>
<%@page import="com.amazonaws.services.cloudwatch.model.Datapoint"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manager View</title>
<style type="text/css">
.divSur {
	border-style: solid;
}
</style>
</head>

<body>
	<script type="text/javascript">
		function manualGrow() {
			document.display.action = "../ManualGrowServlet";
			document.display.submit();
		}
		function manualShrink() {
			document.display.action = "../ManualShrinkServlet";
			document.display.submit();
		}

		function startAutoScaling() {
			document.display.action = "../StartAutoScadulingServlet";
			document.display.submit();
		}
		function stopAutoScaling() {
			document.display.action = "../StopAutoScadulingServlet";
			document.display.submit();
		}
	</script>

	<div align=center>
		<h1>Manager View</h1>

	</div>
	<p></p>
	<table align=center>

		<%
			CloudWatching cw = (CloudWatching) session
					.getAttribute(GlobalValues.CLOUD_WATCHING);

			// Instead of verifying if it is manager...I do it so roughly...
			if (!((String) session.getAttribute(GlobalValues.PRIVILEGE_TAG))
					.equals(GlobalValues.PRIVILEGE_ADMIN)) {
				cw = null;
			}
			List<CloudWatcher> watchers = cw.getCPUUtilization();
		%>
		<tr>
			<td><b><%=watchers.size()%> of <%=cw.getAllEC2instances().size()%>
					instances is running. <input type="button"
					onclick="javascript:window.location.reload()" value="Reload">
			</b></td>
		</tr>
		<%
			for (CloudWatcher watcher : watchers) {
		%>
		<tr>
			<td><h3>
					<b>Instance ID</b>:
					<%=watcher.getInstanceId()%></h3></td>
		</tr>
		<%
			for (Datapoint datapoint : watcher.getDatapoints()) {
		%>
		<tr>
			<td><b>Time Stamp:</b><%=datapoint.getTimestamp()%></td>
			<td></td>
			<td></td>
			<td><b>CPU Utilization</b>:<%=datapoint.getMaximum()%>%</td>
		</tr>
		<%
			}
			}
		%>
		<tr></tr>
		<tr></tr>
	</table>
	<form name=display action="" method=POST>
		<table align=center>
			<tr>
				<td><h3>
						<u>Manual Scaling</u>
					</h3></td>
			</tr>
			<tr>

				<td>Shrinking Ratio:</td>
				<td><input type=text value=2 name=manualShrinkRatio></td>
				<td><input type=button value=shrink onclick="manualShrink()"></td>
			</tr>
			<tr>
				<td>Growing Ratio:</td>
				<td><input type=text value=2 name=manualGrowRatio></td>
				<td><input type=button value=grow onclick="manualGrow()"></td>
			</tr>

			<tr>
				<td><h3>
						<u>Auto Scaling Configuration</u>
					</h3></td>
			</tr>

			<tr>
				<td>CPU Utilization Threshold:</td>
			</tr>
			<tr>
				<td>Growing Threshold: <input type=text value=80
					name=expandThreshlod>(%)
				</td>
				<td>Ratio: <input type=text value=2 name=growRatio></td>
			</tr>
			<tr>
				<td>Shrinking Threshold: <input type=text value=30
					name=shrinkThreshlod>(%)
				</td>
				<td>Ratio: <input type=text value=2 name=shrinkRatio></td>
			</tr>
			<tr>
				<td></td>
				<td><input type=button value=start onclick="startAutoScaling()">
					<input type="button" value=stop onclick="stopAutoScaling()"></td>
			</tr>

		</table>
	</form>


</body>
</html>