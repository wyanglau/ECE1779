<%@page import="com.amazonaws.services.cloudwatch.model.Datapoint"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ece1779.GlobalValues"%>
<%@page import="ece1779.loadBalance.*"%>
<%@page import="ece1779.commonObjects.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Timer"%>
<%@page import="com.amazonaws.services.cloudwatch.model.Datapoint"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
<title>Manager View</title>
<style type="text/css">
.divSur {
	border-style: solid;
	width: 650px;
}

.reloadbtn {
	background-color: #44c767;
	-moz-border-radius: 6px;
	-webkit-border-radius: 6px;
	border-radius: 6px;
	border: 1px solid #18ab29;
	display: inline-block;
	cursor: pointer;
	color: #ffffff;
	font-family: arial;
	font-size: 16px;
	padding: 4px 4px;
	text-decoration: none;
	text-shadow: 0px 1px 0px #2f6627;
}

.reloadbtn:hover {
	background-color: #5cbf2a;
}

.reloadbtn:active {
	position: relative;
	top: 1px;
}
</style>
</head>

<body>
	<script type="text/javascript">
		function manualGrow() {
			$
					.ajax({
						type : "POST",
						url : "../ManualGrowServlet",
						data : {
							manualGrowRatio : $("#manualGrowRatio").val()
						},
						success : function(result) {
							if (result == "SUCCESS") {
								alert("Successful Operation. Please wait for the startup of instances.")
							} else if (result == "INVALID_PARAMETER") {
								alert("Invalid input of ratio, only integer larger than 1 acceptable.")
							} else {
								alert("AWS Error incured, try again later.")
							}
						}
					})
		}
		function manualShrink() {
			$
					.ajax({
						type : "POST",
						url : "../ManualShrinkServlet",
						data : {
							manualShrinkRatio : $("#manualShrinkRatio").val()
						},
						success : function(result) {
							if (result == "SUCCESS") {
								alert("Successful Operation. Please wait for the shutdown of instances.")
							} else if (result == "INVALID_PARAMETER") {
								alert("Invalid input of ratio, only integer larger than 1 acceptable.")
							} else {
								alert("AWS Error incured, try again later.")
							}
						}
					})
		}
		function stopAutoScaling() {
			$.ajax({
				type : "POST",
				url : "../StopAutoScadulingServlet",
				success : function(result) {
					document.getElementById("stopAuto").disabled = true;
					document.getElementById("startAuto").disabled = false;
				}
			})
		}

		function startAutoScaling() {
			document.getElementById("stopAuto").disabled = false;
			document.getElementById("startAuto").disabled = true;
			$
					.ajax({
						type : "POST",
						url : "../StartAutoScadulingServlet",
						data : {
							expandThreshlod : $("#expandThreshlod").val(),
							shrinkThreshlod : $("#shrinkThreshlod").val(),
							growRatio : $("#growRatio").val(),
							shrinkRatio : $("#growRatio").val()
						},
						success : function(result) {

							if (result == "SUCCESS") {
								alert("Auto-Scaling started up.");
							} else if (result == "INVALID_PARAMETER") {
								alert("Invalid parameters.Required Pattern: Growing Threshold > Shrinking Threshold > 0 , Ratios should be integers and > 2")
								document.getElementById("stopAuto").disabled = true;
								document.getElementById("startAuto").disabled = false;
							} else {
								alert("Error incured while starting auto-scaling.");
								document.getElementById("stopAuto").disabled = true;
								document.getElementById("startAuto").disabled = false;
							}
						}
					})
		}
	</script>
	<div align=center>
		<h1>Manager View</h1>

	</div>
	<p></p>

	<table align=center>

		<%
			Timer timer = (Timer) session.getAttribute("Timer");
			String stopTag = "";
			String startTag = "";
			if (timer == null) {
				startTag = "";
				stopTag = "disabled=disabled";
			} else {
				stopTag = "";
				startTag = "disabled=disabled";
			}

			CloudWatching cw = (CloudWatching) session
					.getAttribute(GlobalValues.CLOUD_WATCHING);

			List<CloudWatcher> watchers = cw.getCPUUtilization();
		%>
		<tr>
			<td><b><%=watchers.size()%> of <%=cw.getAllEC2instances().size()%>
					instances is running. <a href="#" class="reloadbtn"
					onclick="javascript:window.location.reload()">Reload</a> </b></td>
		</tr>
	</table>
	<div align=center>
		<div class=divSur>
			<table>
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
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
			</table>
		</div>
	</div>
	<form name=display action="" method=POST>
		<table align=center>
			<tr>
				<td><h3>
						<u>Manual Scaling</u>
					</h3></td>
			</tr>
			<tr>

				<td>Shrinking Ratio:</td>
				<td><input type=text value=2 name=manualShrinkRatio
					id=manualShrinkRatio></td>
				<td><input type=button value=shrink onclick="manualShrink()"></td>
			</tr>
			<tr>
				<td>Growing Ratio:</td>
				<td><input type=text value=2 name=manualGrowRatio
					id=manualGrowRatio></td>
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
					name=expandThreshlod id=expandThreshlod>(%)
				</td>
				<td>Ratio: <input type=text value=2 name=growRatio id=growRatio></td>
				<td><input id=startAuto type=button value=start
					onclick="startAutoScaling()" <%=startTag%>></td>
			</tr>
			<tr>
				<td>Shrinking Threshold: <input type=text value=30
					name=shrinkThreshlod id=shrinkThreshlod>(%)
				</td>
				<td>Ratio: <input type=text value=2 name=shrinkRatio
					id=shrinkRatio></td>
				<td><input type="button" id=stopAuto value=stop <%=stopTag%>
					onclick="stopAutoScaling()"></td>
			</tr>
			<tr>
				<td></td>

			</tr>

		</table>
	</form>


</body>
</html>