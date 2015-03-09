package ece1779.DAO;

import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import ece1779.GlobalValues;

public class MngrDBOperations {

	private SharedPoolDataSource dbcp;

	public MngrDBOperations(SharedPoolDataSource dbcp) throws SQLException {
		this.dbcp = dbcp;

	}

	/**
	 * clean up MySQL
	 * 
	 * @throws SQLException
	 */
	public void deleteAllDB() throws SQLException {
		Statement statement = this.dbcp.getConnection().createStatement();
		try {

			// delete everything from both the users and images table in the
			// database
			statement
					.executeUpdate("DELETE FROM " + GlobalValues.dbTable_Users);
			statement.executeUpdate("DELETE FROM "
					+ GlobalValues.dbTable_Images);

		} finally {
			if (statement != null)
				statement.close();

		}
	}
}