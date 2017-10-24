package ece1779.userOperations;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.im4java.core.IM4JavaException;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.DAO.UserDBOperations;
import ece1779.DAO.UserS3Operations;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

public class UserOperations {

	private User user;
	private BasicAWSCredentials credentials;
	private SharedPoolDataSource dbcp;

	public UserOperations(User user, BasicAWSCredentials credentials,
			SharedPoolDataSource dbcp) {
		this.user = user;
		this.credentials = credentials;
		this.dbcp = dbcp;

	}

	/**
	 * Load all images of current user
	 * 
	 * @throws SQLException
	 */
	public void load() throws SQLException {

		UserDBOperations db = new UserDBOperations(this.user, this.dbcp);
		this.user.setImgs(db.findAllImgs());

	}

	/**
	 * Transformed and Save New Uploaded Images
	 * 
	 * @param user
	 * @throws IOException
	 * @throws IM4JavaException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public Images uploadAndSave(File image) throws IOException,
			InterruptedException, IM4JavaException, SQLException {

		ImageProcessing ip = new ImageProcessing();
		List<File> images = ip.transform(image);

		Images imgObj = new Images(user.getId(), -1, null);

		// save to s3
		UserS3Operations s3 = new UserS3Operations(credentials, user);
		s3.save(images, imgObj);

		// update database, retrieve image ID from database and return the
		// imgObj
		// note: addImage returns int imageID from SQL right after image has
		// been added
		UserDBOperations db = new UserDBOperations(user, dbcp);
		imgObj.setImgId(db.addImage(imgObj));

		// add it to current user
		user.addImage(imgObj);
		return imgObj;

	}

}
