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

	/**
	 * 
	 * Save images to s3 bucket ece1779winter2015group14number1 ,
	 * 
	 * the format of key is imageId_UUID, e.g.
	 * 
	 * 123_d6310487-f3aa-4c62-81ab-79748b8975fa
	 * 
	 */
	public void save(List<File> files, BasicAWSCredentials awsCredentials,
			Images image) throws IOException {

		List<String> keys = new ArrayList<String>();
		for (int i = 0; i < files.size(); i++) {
			String key = image.getImgId() + "_" + UUID.randomUUID();
			this.save(files.get(i), key, awsCredentials);
			keys.add(key);
		}
		image.setKeys(keys);
	}

	/**
	 * 
	 * save specified file if you knew the key of object.
	 * 
	 */
	public void save(File file, String key, BasicAWSCredentials awsCredentials)
			throws IOException {

		AmazonS3 s3 = new AmazonS3Client(awsCredentials);
		String bucketName = GlobalValues.BUCKET_NAME;
		try {

			if (!s3.doesBucketExist(bucketName)) {
				s3.createBucket(bucketName);
				System.out.println("Creating new bucket " + bucketName);
			}

			s3.putObject(new PutObjectRequest(bucketName, key, file));
			s3.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);

		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}

	public void deleteALl(BasicAWSCredentials awsCredentials) {
		AmazonS3 s3 = new AmazonS3Client(awsCredentials);
		String bucketName = GlobalValues.BUCKET_NAME;
		;
		try {

			ObjectListing objects = s3.listObjects(bucketName);

			List<S3ObjectSummary> summaries = objects.getObjectSummaries();
			for (S3ObjectSummary item : summaries) {
				String key = item.getKey();
				s3.deleteObject(bucketName, key);
				System.out.println("Delet obj key = " + key);
			}

			while (objects.isTruncated()) {
				objects = s3.listNextBatchOfObjects(objects);
				summaries = objects.getObjectSummaries();
				for (S3ObjectSummary item : summaries) {
					String key = item.getKey();
					s3.deleteObject(bucketName, key);
				}
			}

		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
		}

	}

	public static void main(String[] args) {

		String accessKey = "AKIAJCYDX3Y5VZ5PYYFA";
		String secretKey = "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";
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		BasicS3Operation s3 = new BasicS3Operation();
		
		// // save
		// File file = new File("Sources/doraemon.jpg");
		// String key = "MyObjectKey_" + UUID.randomUUID();
		// try {
		// s3.save(file, key, awsCredentials, null);
		// System.out.println("~~~~~?");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// //delete
		// s3.deleteALl(awsCredentials);
		System.out.println("Finished");
	}

}
