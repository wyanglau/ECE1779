package ece1779.loadBalance;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.amazonaws.services.opsworks.model.StartInstanceRequest;
import com.amazonaws.services.opsworks.model.StopInstanceRequest;

import ece1779.GlobalValues;

public class InstancesOperations {

	private AmazonEC2 ec2;

	public InstancesOperations(AWSCredentials credentials) {
		ec2 = new AmazonEC2Client(credentials);
	}

	public InstancesOperations(AmazonEC2Client ec2) {
		this.ec2 = ec2;
	}

	/**
	 * 
	 * @param quantity
	 */
	public RunInstancesResult runInstances(int quantity)
			throws AmazonServiceException, AmazonClientException {

		String ami = GlobalValues.AMI_ID;

		RunInstancesRequest request = new RunInstancesRequest(ami, quantity,
				quantity);
		request.setKeyName("ECE1779-GROUP14");
		request.withSecurityGroups("ECE1779-GROUP14");
		RunInstancesResult result = ec2.runInstances(request);

		// set tag
		List<String> ids = new ArrayList<String>();
		Tag tag = new Tag("Name", "ECE1779-GROUP14");
		Reservation reservation = result.getReservation();
		for (Instance instance : reservation.getInstances()) {
			ids.add(instance.getInstanceId());
		}
		addTag(tag, ids);
		return result;

	}

	/**
	 * Create or Override tag for resources of input ids
	 * 
	 * @param tag
	 * @param id
	 */
	private void addTag(Tag tag, List<String> ids)
			throws AmazonServiceException, AmazonClientException {

		CreateTagsRequest request = new CreateTagsRequest();
		request.setResources(ids);
		request.withTags(tag);
		ec2.createTags(request);

	}

	public TerminateInstancesResult terminateInstances(List<String> ids)
			throws AmazonServiceException, AmazonClientException {
		TerminateInstancesRequest request = new TerminateInstancesRequest(ids);
		return ec2.terminateInstances(request);
	}

	public StopInstancesResult stopInstances(List<String> ids)
			throws AmazonServiceException, AmazonClientException {
		StopInstancesRequest request = new StopInstancesRequest(ids);
		return ec2.stopInstances(request);
	}

	public StartInstancesResult startInstances(List<String> ids)
			throws AmazonServiceException, AmazonClientException {
		StartInstancesRequest request = new StartInstancesRequest(ids);
		return ec2.startInstances(request);
	}

	public static void main(String[] args) {
		String accessKey = "";
		String secretKey = "";
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		InstancesOperations op = new InstancesOperations(awsCredentials);
		
		//run
		//		op.runInstances(2);
		
		List<String> ids = new ArrayList<String>();
		ids.add("i-b87ac942");
		op.stopInstances(ids);
		
		
		System.out.println("Done");
	}
}
