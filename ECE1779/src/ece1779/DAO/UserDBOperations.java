package ece1779.DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ece1779.GlobalValues;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

import java.sql.*;

public class UserDBOperations {

	private User user;
	private Statement statement;

	public UserDBOperations(User user, Statement st) {
		this.user = user;
		this.statement = st;
	}

	/**
	 * search for images belongs to this.user from sql
	 */
	public List<Images> findImgs() throws SQLException {
	    ResultSet rs = null;
		try {
			// retrieve list of all image sets belonging to current user
			rs = statement.executeQuery("select * from " + GlobalValues.dbTable_Images + " where userid='" + user.getId()
					+ "'");
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
	        if (rs != null) try { rs.close(); } catch (SQLException logOrIgnore) {}
	    }
	}

	/**
	 * search for user id from MySQL
	 * 
	 * @return int
	 * int = -1 if User not found
	 */
	public int findUserID () throws SQLException {
	    ResultSet rs = null;
		try {
			// Retrieve information from database with given username and password
			rs = statement.executeQuery("select * from " + GlobalValues.dbTable_Users + " where login='" + user.getUserName()
					+ "'");
			
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
	        if (rs != null) try { rs.close(); } catch (SQLException logOrIgnore) {}
	    }
	}

	/**
	 * search for user password from MySQL
	 * 
	 * @return String
	 * String = null if User not found
	 */
	public String findUserPW () throws SQLException {
	    ResultSet rs = null;
		try {
			// Retrieve information from database with given username and password
			rs = statement.executeQuery("select * from " + GlobalValues.dbTable_Users + " where login='" + user.getUserName()
					+ "'");
			
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
	        if (rs != null) try { rs.close(); } catch (SQLException logOrIgnore) {}
	    }
	}
		
	public boolean addUser (String pwd) throws SQLException {
		ResultSet rs = null;
		try {
			// Enter data into database
			// int i = 1 if successful
			// int i = 0 if fail
			int i = statement.executeUpdate("insert into users(login, password) values ('"+ user.getUserName() + "','" + pwd + "')");
			
			// Successful write to SQL database
			if (i > 0) {
				return true;
			}
			//Write failed
			else {
				return false;
			}
			
		} finally {
	        if (rs != null) try { rs.close(); } catch (SQLException logOrIgnore) {}
	    }
	}

	public void addImages(Images imageObj) {

		
		/**
		 * 1. insert the keys in imageObj to the specified userId. 
		 * 2. set the key of this image to the input imageObj
		 */
	}

}
