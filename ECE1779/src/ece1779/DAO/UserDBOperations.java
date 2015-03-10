package ece1779.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import ece1779.GlobalValues;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

public class UserDBOperations {

	private User user;
	private SharedPoolDataSource dbcp;

	public UserDBOperations(User user, SharedPoolDataSource dbcp) {
		this.user = user;
		this.dbcp = dbcp;
	}

	/**
	 * search for all images belongs to this.user from sql
	 */
	public List<Images> findAllImgs() throws SQLException {
		ResultSet rs = null;
		Connection con = this.dbcp.getConnection();
		Statement statement = con.createStatement();
		try {
			// retrieve list of all image sets belonging to current user
			rs = statement.executeQuery("select * from "
					+ GlobalValues.dbTable_Images + " where userId='"
					+ user.getId() + "'");
			// create an array of img sets
			List<Images> imgSet = new ArrayList<Images>();

			// fill img set with all resultset results
			while (rs.next()) {
				// create new array of img URLs in the img set
				List<String> keys = new ArrayList<String>();
				keys.add(rs.getString("key1"));
				keys.add(rs.getString("key2"));
				keys.add(rs.getString("key3"));
				keys.add(rs.getString("key4"));
				// create an instance of imageset -> Images
				Images img = new Images(user.getId(), rs.getInt("id"), keys);
				imgSet.add(img);
			}

			return imgSet;

		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}
				if (rs != null)

					rs.close();
			} catch (SQLException logOrIgnore) {
			}
		}
	}

	/**
	 * search for user id from MySQL
	 * 
	 * @return int int = -1 if User not found
	 */
	public int findUserID() throws SQLException {
		ResultSet rs = null;
		Connection con = this.dbcp.getConnection();
		Statement statement = con.createStatement();
		try {
			// Retrieve information from database with given username and
			// password
			rs = statement.executeQuery("select * from "
					+ GlobalValues.dbTable_Users + " where login='"
					+ user.getUserName() + "'");

			// Username Exists
			if (rs.first()) {
				// get user id from id column of database
				return rs.getInt("id");
			}
			// Username Doesn't Exist
			else {
				return -1;
			}

		} finally {
			try {
				if (rs != null)

					rs.close();

				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException logOrIgnore) {
			}
		}
	}

	/**
	 * search for user password from MySQL
	 * 
	 * @return String String = null if User not found
	 */
	public String findUserPW() throws SQLException {
		ResultSet rs = null;
		Connection con = this.dbcp.getConnection();
		Statement statement = con.createStatement();
		try {
			// Retrieve information from database with given username and
			// password
			rs = statement.executeQuery("select * from "
					+ GlobalValues.dbTable_Users + " where login='"
					+ user.getUserName() + "'");

			// Username Exists
			if (rs.first()) {
				// get user pw from password column of database
				return rs.getString("password");
			}
			// Username Doesn't Exist
			else {
				return null;
			}

		} finally {
			try {
				if (rs != null)

					rs.close();

				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException logOrIgnore) {
			}
		}
	}

	/**
	 * add user to MySQL
	 * 
	 * @return boolean boolean = 1 if success, 0 if fail
	 */
	public boolean addUser(String pwd) throws SQLException {
		// Enter data into database
		Connection con = this.dbcp.getConnection();
		Statement statement = con.createStatement();
		try {

			int i = statement.executeUpdate("insert into "
					+ GlobalValues.dbTable_Users
					+ "(login, password) values ('" + user.getUserName()
					+ "','" + pwd + "')");

			// Successful write to SQL database
			if (i > 0) {
				return true;
			}
			// Write failed
			else {
				return false;
			}
		} finally {
			try {

				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException logOrIgnore) {
			}
		}
	}

	/**
	 * add image to MySQL
	 * 
	 * @return int int = image id from SQL database
	 */
	public int addImage(Images imageObj) throws SQLException {
		System.out.println("[UserDBOperations] Saving images to database.");
		ResultSet rs = null;
		Connection con = this.dbcp.getConnection();
		Statement statement = con.createStatement();

		try {
			// Enter data into database
			statement.executeUpdate("insert into "
					+ GlobalValues.dbTable_Images
					+ "(userId, key1, key2, key3, key4) values ('"
					+ user.getId() + "','" + imageObj.getKeys().get(0) + "','"
					+ imageObj.getKeys().get(1) + "','"
					+ imageObj.getKeys().get(2) + "','"
					+ imageObj.getKeys().get(3) + "')");

			// Retrieve image from SQL after adding it
			rs = statement.executeQuery("select * from "
					+ GlobalValues.dbTable_Images + " where key1='"
					+ imageObj.getKeys().get(0) + "'");

			// return the image id
			if (rs.first()) {
				// get image id from id column of database
				return rs.getInt("id");
			}
			// Image didn't save into database somehow
			else {
				return -1;
			}

		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}
				if (rs != null)
					rs.close();
			} catch (SQLException logOrIgnore) {
			}
		}
	}

}
