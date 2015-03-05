package ece1779.userOperations;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.DAO.UserDBOperations;
import ece1779.DAO.UserS3Operations;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

public class UserOperations {

	private User user;
	private BasicAWSCredentials credentials;

	public UserOperations(User user, BasicAWSCredentials credentials) {
		this.user = user;
		this.credentials = credentials;
	}

	/**
	 * Load all images of current user
	 */
	public void load() {

		UserDBOperations db = new UserDBOperations(this.user);
		this.user.setImgs(db.findImgs());

	}

	/**
	 * Transformed and Save New Uploaded Images
	 * 
	 * @param user
	 * @throws IOException
	 */
	public Images uploadAndSave(File image) throws IOException {

		ImageProcessing ip = new ImageProcessing();
		List<File> images = ip.transform(image);

		Images imgObj = new Images(user.getId(), -1, null);

		// save to s3
		UserS3Operations s3 = new UserS3Operations(credentials, user);
		s3.save(images, imgObj);

		// update database
		UserDBOperations db = new UserDBOperations(user);
		db.addImages(imgObj);
		return imgObj;

	}

}
