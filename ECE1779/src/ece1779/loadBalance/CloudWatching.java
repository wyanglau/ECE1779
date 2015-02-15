package ece1779.loadBalance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.Metric;

import ece1779.commonObjects.CloudWatcher;

public class CloudWatching {

	@SuppressWarnings("finally")
	public List<CloudWatcher> getCPUUtilization(
			BasicAWSCredentials awsCredentials) {
		List<CloudWatcher> statistics = new ArrayList<CloudWatcher>();
		AmazonCloudWatch cw = new AmazonCloudWatchClient(awsCredentials);
		try {
			ListMetricsRequest listMetricsRequest = new ListMetricsRequest();
			listMetricsRequest.setMetricName("CPUUtilization");
			listMetricsRequest.setNamespace("AWS/EC2");
			ListMetricsResult result = cw.listMetrics(listMetricsRequest);
			List<Metric> metrics = result.getMetrics();
			for (Metric metric : metrics) {
				statistics.add(getStatistics(metric, cw));
			}

		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
		} finally {
			return statistics;
		}
	}

	private CloudWatcher getStatistics(Metric metric, AmazonCloudWatch cw) {

		String namespace = metric.getNamespace();
		String metricName = metric.getMetricName();
		List<Dimension> dimensions = metric.getDimensions();
		GetMetricStatisticsRequest statisticsRequest = new GetMetricStatisticsRequest();
		statisticsRequest.setNamespace(namespace);
		statisticsRequest.setMetricName(metricName);
		statisticsRequest.setDimensions(dimensions);
		Date endTime = new Date();
		Date startTime = new Date();
		startTime.setTime(endTime.getTime() - 1200000);
		statisticsRequest.setStartTime(startTime);
		statisticsRequest.setEndTime(endTime);
		statisticsRequest.setPeriod(300); // it doesn't matter what is the
											// number here..
		Vector<String> statistics = new Vector<String>();
		statistics.add("Maximum");
		statisticsRequest.setStatistics(statistics);
		GetMetricStatisticsResult stats = cw
				.getMetricStatistics(statisticsRequest);
		System.out.println("Namespace = " + namespace + " Metric = "
				+ metricName + " Dimensions = " + dimensions);
		System.out.println("Values = " + stats.toString());

		return parseStatistics(dimensions, stats);
	}

	private CloudWatcher parseStatistics(List<Dimension> dimensions,
			GetMetricStatisticsResult stats) {
		return new CloudWatcher(dimensions.get(0).getValue(),
				stats.getDatapoints());
	}

	public static void main(String[] args) {
		String accessKey = "";
		String secretKey = "";
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);

		CloudWatching cloud = new CloudWatching();
		List<CloudWatcher> result = cloud
				.getCPUUtilization(awsCredentials);

		for (CloudWatcher w : result) {
			System.out.println("instance id :" + w.getInstanceId());
			System.out.println("namespace " + w.getNameSpace() + "||"
					+ "statistic" + w.getStatistic());
			System.out.println("datapoints:" + w.getDatapoints().toString());
		}
	}
}
