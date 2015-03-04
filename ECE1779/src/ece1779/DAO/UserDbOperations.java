package ece1779.DAO;

import java.io.File;
import java.util.List;

import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

public class UserDbOperations {

	private User user;

	public UserDbOperations(User user) {
		this.user = user;
	}

	/**
	 * search for images belongs to this.user from S3
	 */
	public List<Images> findImgs() {

		return null;
	}

	/**
	 * search for user id from MySQL
	 * 
	 * @return
	 */
	public int findUserId() {

		return 1;
	}

	public void addImages(List<File> imgs) {

		/**
		 *  1. 存到S3， 得到链接（key）
		 *  2. 把KEY存到MYSQL中相应的表里
		 *  3. 新建commonObjects.Images对象，依次把4张图片的链接初始化到Images对象中，再添加到当前this.user里
		 *  
		 */
	}

}
