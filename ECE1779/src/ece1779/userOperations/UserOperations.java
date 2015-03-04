package ece1779.userOperations;


import java.io.File;

import ece1779.DAO.UserDBOperations;
import ece1779.commonObjects.User;

public class UserOperations {

	private User currentUser;

	public UserOperations(User user) {
		this.currentUser = user;
	}
	
	/**
	 * Load all images of current user
	 */
	public void load() {

		UserDBOperations db = new UserDBOperations(this.currentUser);
		this.currentUser.setImgs(db.findImgs());

	}

	/**
	 * Transformed and Save New Uploaded Images
	 * 
	 * @param user
	 */
	public void uploadAndSave(File image) {
		/**
		 * 1. 调ImageProcessing类的方法做transformation
		 * 2. 调UserDbOperations里的S3存储方法进行存储
		 */
	}

}
