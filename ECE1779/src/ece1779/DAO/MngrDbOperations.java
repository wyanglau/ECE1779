package ece1779.DAO;

import java.io.File;
import java.util.List;

import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

public class MngrDbOperations {

	private User user;

	public MngrDbOperations(User user) {
		this.user = user;
	}

	
	public void deleteAll() {

		/**
		 * 1. clean up S3
		 * 2. clean up MySQL
		 */
	}
}