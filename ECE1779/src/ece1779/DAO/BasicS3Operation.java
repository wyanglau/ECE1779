package ece1779.DAO;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class BasicS3Operation extends HttpServlet {

	public void s3SaveFile(File file, String key, PrintWriter out)
			throws IOException {

		BasicAWSCredentials awsCredentials = (BasicAWSCredentials) this
				.getServletContext().getAttribute("AWSCredentials");

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

}
