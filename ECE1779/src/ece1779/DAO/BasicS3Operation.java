package ece1779.DAO;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class BasicS3Operation {

	public void s3SaveFile(File file, String key,
			BasicAWSCredentials awsCredentials, PrintWriter out)
			throws IOException {

		AmazonS3 s3 = new AmazonS3Client(awsCredentials);

		String bucketName = "ece1779winter2015group14";

		try {
			s3.putObject(new PutObjectRequest(bucketName, key, file));

			s3.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
		} catch (AmazonServiceException ase) {
			out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			out.println("Error Message:    " + ase.getMessage());
			out.println("HTTP Status Code: " + ase.getStatusCode());
			out.println("AWS Error Code:   " + ase.getErrorCode());
			out.println("Error Type:       " + ase.getErrorType());
			out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			out.println("Error Message: " + ace.getMessage());
		}
	}

	public void deleteALlData() {

	}

	public static void main(String[] args) {
		String accessKey = "AKIAJCYDX3Y5VZ5PYYFA";
		String secretKey = "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";
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		BasicS3Operation s3 = new BasicS3Operation();
		File file = new File("Sources/560_350_cat.gif");
		String key = "MyObjectKey_" + UUID.randomUUID();
		try {
			s3.s3SaveFile(file, key, awsCredentials, null);
			System.out.println("~~~~~?");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
