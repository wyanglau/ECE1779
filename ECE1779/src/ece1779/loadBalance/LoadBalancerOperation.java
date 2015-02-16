package ece1779.loadBalance;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.CreateLoadBalancerRequest;
import com.amazonaws.services.elasticloadbalancing.model.CreateLoadBalancerResult;
import com.amazonaws.services.elasticloadbalancing.model.DeregisterInstancesFromLoadBalancerRequest;
import com.amazonaws.services.elasticloadbalancing.model.DeregisterInstancesFromLoadBalancerResult;
import com.amazonaws.services.elasticloadbalancing.model.Listener;
import com.amazonaws.services.elasticloadbalancing.model.RegisterInstancesWithLoadBalancerRequest;
import com.amazonaws.services.elasticloadbalancing.model.RegisterInstancesWithLoadBalancerResult;

import ece1779.GlobalValues;

public class LoadBalancerOperation {
	private AmazonEC2 ec2;
	private AmazonElasticLoadBalancing elb;
	private String loadBalancerName;

	public LoadBalancerOperation(AWSCredentials awsCredentials,
			String loadBalancerName) {
		ec2 = new AmazonEC2Client(awsCredentials);
		elb = new AmazonElasticLoadBalancingClient(awsCredentials);
		this.loadBalancerName = loadBalancerName;

	}

	public LoadBalancerOperation(AmazonEC2Client ec2,
			AWSCredentials awsCredentials, String loadBalancerName) {
		this.ec2 = ec2;
		elb = new AmazonElasticLoadBalancingClient(awsCredentials);
		this.loadBalancerName = loadBalancerName;
	}

	/**
	 * <b>Default Setting<br>
	 * </b> Listener: HTTP 80 :8080<br>
	 * AvailabilityZone: us-east-1a<br>
	 * SecurityGroup:sg-36c76d52(ECE1779-GROUP14)<br>
	 * EableCrossZone: disabled<br>
	 *
	 * @return CreateLoadBalancerResult result / null if catch exception
	 */
	public CreateLoadBalancerResult createLoadBalancer()
			throws AmazonServiceException, AmazonClientException {

		CreateLoadBalancerRequest request = new CreateLoadBalancerRequest(
				this.loadBalancerName);
		// set availability zone
		request.withAvailabilityZones("us-east-1a");

		// set listener
		Listener listener = new Listener("HTTP", 80, 8080);
		List<Listener> listeners = new ArrayList<Listener>();
		listeners.add(listener);
		request.setListeners(listeners);
		// set security group
		request.withSecurityGroups(GlobalValues.SECURITY_GROUP_ID);

		// create load balancer
		CreateLoadBalancerResult result = elb.createLoadBalancer(request);
		System.out.println("Created Load Balancer " + this.loadBalancerName
				+ ".");
		return result;

	}

	/**
	 * Add ec2 instances to load balancer
	 * 
	 * @param ids
	 *            instances' id
	 * 
	 * @return null if catch exception
	 */
	public RegisterInstancesWithLoadBalancerResult register(List<String> ids)
			throws AmazonServiceException, AmazonClientException {
		List<com.amazonaws.services.elasticloadbalancing.model.Instance> instances = new ArrayList<com.amazonaws.services.elasticloadbalancing.model.Instance>();

		for (String id : ids) {
			instances
					.add(new com.amazonaws.services.elasticloadbalancing.model.Instance(
							id));
		}
		RegisterInstancesWithLoadBalancerRequest request = new RegisterInstancesWithLoadBalancerRequest(
				loadBalancerName, instances);
		System.out.println("Register instances " + ids.toString()
				+ " from load balancer:" + loadBalancerName);
		return elb.registerInstancesWithLoadBalancer(request);

	}

	/**
	 * Remove ec2 instances from load balancer
	 * 
	 * @param ids
	 *            instances' id
	 * @return null if catch exception
	 */
	public DeregisterInstancesFromLoadBalancerResult remove(List<String> ids)
			throws AmazonServiceException, AmazonClientException {
		List<com.amazonaws.services.elasticloadbalancing.model.Instance> instances = new ArrayList<com.amazonaws.services.elasticloadbalancing.model.Instance>();

		for (String id : ids) {
			instances
					.add(new com.amazonaws.services.elasticloadbalancing.model.Instance(
							id));
		}
		DeregisterInstancesFromLoadBalancerRequest request = new DeregisterInstancesFromLoadBalancerRequest(
				loadBalancerName, instances);
		System.out.println("Remove instances " + ids.toString()
				+ " from load balancer:" + loadBalancerName);
		return elb.deregisterInstancesFromLoadBalancer(request);

	}

	/**
	 * 
	 * @return null if catch exception
	 */
	public List<Instance> getAllEC2instances() throws AmazonServiceException,
			AmazonClientException {

		DescribeInstancesResult result = ec2.describeInstances();
		List<Reservation> reservations = result.getReservations();
		List<Instance> instances = new ArrayList<Instance>();
		for (Reservation r : reservations) {
			instances.addAll(r.getInstances());
		}
		return instances;

	}

	public static void main(String[] args) {
		String accessKey = "";
		String secretKey = "";
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		LoadBalancerOperation lb = new LoadBalancerOperation(awsCredentials,
				"group14-lb");

		List<String> ids = new ArrayList<String>();
		List<Instance> instances = lb.getAllEC2instances();
		for (Instance instance : instances) {
			ids.add(instance.getInstanceId());
		}
		System.out.println(ids.toString());
		// create
		// lb.createLoadBalancer();

		// register

		// lb.register(ids);

		// remove
		// lb.remove(ids);

	}

}
