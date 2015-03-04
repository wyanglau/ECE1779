package ece1779.DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import ece1779.GlobalValues;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

public class MngrDBOperations {

	private User user;

	public MngrDBOperations(User user) {
		this.user = user;
	}


	/**
	 * clean up MySQL
	 */
	public void deleteAllDB() {
		Connection con = null;
	    Statement st = null;
		try {
			// Create connection to database
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://"
					+ GlobalValues.dbLocation_URL + ":"
					+ GlobalValues.dbLocation_Port + "/"
					+ GlobalValues.dbLocation_Schema, GlobalValues.dbAdmin_Name,
					GlobalValues.dbAdmin_Pass);
			st = con.createStatement();

			// delete everything from both the users and images table in the database
			st.executeUpdate("truncate " + GlobalValues.dbTable_Users);
			st.executeUpdate("truncate " + GlobalValues.dbTable_Images);
			
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		} finally {
	        if (st != null) try { st.close(); } catch (SQLException logOrIgnore) {}
	    }
	}
}