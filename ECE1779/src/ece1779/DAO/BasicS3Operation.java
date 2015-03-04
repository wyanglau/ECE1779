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
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import ece1779.GlobalValues;
import ece1779.commonObjects.Images;

public class BasicS3Operation {

	private AmazonS3 s3;

	public BasicS3Operation(BasicAWSCredentials awsCredentials) {
		this.s3 = new AmazonS3Client(awsCredentials);

	}

	/**
	 * 
	 * Save images to s3 bucket ece1779winter2015group14number1 ,
	 * 
	 * the format of key is imageId_UUID, e.g.
	 * 
	 * 123_d6310487-f3aa-4c62-81ab-79748b8975fa
	 * 
	 */
	public void save(List<File> files, Images image) throws IOException {

		List<String> keys = new ArrayList<String>();
		for (int i = 0; i < files.size(); i++) {
			String key = image.getImgId() + "_" + UUID.randomUUID();
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

	public void deleteAll() throws AmazonServiceException,
			AmazonClientException {
		String bucketName = GlobalValues.BUCKET_NAME;
		;

		ObjectListing objects = this.s3.listObjects(bucketName);

		List<S3ObjectSummary> summaries = objects.getObjectSummaries();
		for (S3ObjectSummary item : summaries) {
			String key = item.getKey();
			this.s3.deleteObject(bucketName, key);
			System.out.println("Delet obj key = " + key);
		}

		while (objects.isTruncated()) {
			objects = this.s3.listNextBatchOfObjects(objects);
			summaries = objects.getObjectSummaries();
			for (S3ObjectSummary item : summaries) {
				String key = item.getKey();
				this.s3.deleteObject(bucketName, key);
			}
		}

	}

	public static void main(String[] args) {

		String accessKey = "";
		String secretKey = "/h6Io";
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		BasicS3Operation s3 = new BasicS3Operation(awsCredentials);

		Images image = new Images(0, 1, null);
		List<File> files = new ArrayList<File>();
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
