package ece1779.loadBalance;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.model.Instance;

public class WorkerPoolManagement {
	private BasicAWSCredentials credentials;
	private InstancesOperations op;

	public WorkerPoolManagement(BasicAWSCredentials credentials) {
		this.credentials = credentials;
		op = new InstancesOperations(credentials);
	}

	public void growingByRatio(int ratio) throws AmazonServiceException,
			AmazonClientException {

	}

	public void growingByNumber(int number) throws AmazonServiceException,
			AmazonClientException {

		List<Instance> stoppedInstances = op.getSpecificInstances(1);
		List<String> ids = new ArrayList<String>();
		for (int i = 0; (i < number) && (i < stoppedInstances.size()); i++) {
			ids.add(stoppedInstances.get(i).getInstanceId());
		}

		op.startInstances(ids);

		if (stoppedInstances.size() < number) {
			op.runInstances(number - stoppedInstances.size());
		}

	}

	public void shrinking(int ratio) throws AmazonServiceException,
			AmazonClientException {

	}

	public void shrinkByNumber(int number) {
		List<Instance> runningInstances = op.getSpecificInstances(0);
		List<String> ids = new ArrayList<String>();

		for (int i = 0; (i < number) && (i < runningInstances.size()); i++) {
			ids.add(runningInstances.get(i).getInstanceId());
		}

		op.stopInstances(ids);
	}

	public void cloudWatching() {

	}

}
