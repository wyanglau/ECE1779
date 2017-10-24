package ece1779.DAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import ece1779.GlobalValues;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

public class UserS3Operations {

	private AmazonS3 s3;
	private User user;

	public UserS3Operations(BasicAWSCredentials awsCredentials, User user) {
		this.s3 = new AmazonS3Client(awsCredentials);
		this.user = user;

	}

	/**
	 * 
	 * Save images to s3 bucket ece1779winter2015group14number1 ,
	 * 
	 * the format of key is userId_UUID, e.g.
	 * 
	 * 123_d6310487-f3aa-4c62-81ab-79748b8975fa
	 * 
	 */
	public void save(List<File> files, Images image) throws IOException {

		List<String> keys = new ArrayList<String>();
		for (int i = 0; i < files.size(); i++) {
			String key = user.getId() + "_" + UUID.randomUUID();
			System.out.println("[UserS3Operations] Saving " + key + " to S3.");
			this.save(files.get(i), key);
			keys.add(key);
		}
		image.setKeys(keys);
	}

	/**
	 * 
	 * save specified file if you knew the key of object.
	 * 
	 */
	public void save(File file, String key) throws IOException,
			AmazonServiceException, AmazonClientException {

		String bucketName = GlobalValues.BUCKET_NAME;
		if (!this.s3.doesBucketExist(bucketName)) {
			this.s3.createBucket(bucketName);
			System.out.println("Creating new bucket " + bucketName);
		}
		this.s3.putObject(new PutObjectRequest(bucketName, key, file));
		this.s3.setObjectAcl(bucketName, key,
				CannedAccessControlList.PublicRead);

	}

	public static void main(String[] args) {

		// String accessKey = "";
		// String secretKey = "/h6Io";
		// BasicAWSCredentials awsCredentials = new
		// BasicAWSCredentials(accessKey,
		// secretKey);
		// UserS3Operations s3 = new UserS3Operations(awsCredentials, new
		// User(1,
		// null, null));
		//
		// Images image = new Images(0, 1, null);
		// List<File> files = new ArrayList<File>();
		// save
		// for (int i = 0; i < 4; i++) {
		// File file = new File("Sources/doraemon.jpg");
		//
		// files.add(file);
		// }
		// try {
		// s3.save(files, awsCredentials, image);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// //delete
		// s3.deleteAll(awsCredentials);
		System.out.println("Finished");
	}

}
