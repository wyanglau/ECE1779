package ece1779.DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ece1779.GlobalValues;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

import java.sql.*;

public class UserDBOperations {

	private User user;

	public UserDBOperations(User user) {
		this.user = user;
	}

	/**
	 * search for images belongs to this.user from sql
	 */
	public List<Images> findImgs() {
		try {
			// Create connection to database
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://"
					+ GlobalValues.dbLocation_URL + ":"
					+ GlobalValues.dbLocation_Port + "/"
					+ GlobalValues.dbLocation_Schema, GlobalValues.dbAdmin_Name,
					GlobalValues.dbAdmin_Pass);
			Statement st = con.createStatement();

			// retrieve list of all image sets belonging to current user
			ResultSet rs;
			rs = st.executeQuery("select * from " + GlobalValues.dbTable_Images + " where userid='" + user.getId()
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
		
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * search for user id from MySQL
	 * 
	 * @return int
	 */
	public int findUserId() {
		try {
			// Create connection to database
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://"
					+ GlobalValues.dbLocation_URL + ":"
					+ GlobalValues.dbLocation_Port + "/"
					+ GlobalValues.dbLocation_Schema, GlobalValues.dbAdmin_Name,
					GlobalValues.dbAdmin_Pass);
			Statement st = con.createStatement();

			// Retrieve information from database with given username and password
			ResultSet rs;
			rs = st.executeQuery("select * from " + GlobalValues.dbTable_Users + " where login='" + GlobalValues.USERNAME
					+ "' and password='" + GlobalValues.PASSWORD + "'");

			// get user id from id column of database
			return rs.getInt("id");
			
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return -2;
		} catch (ClassNotFoundException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return -2;
		}
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
