package ece1779.DAO;

import java.sql.SQLException;
import java.sql.Statement;

import ece1779.GlobalValues;

public class MngrDBOperations {

	private Statement statement;

	public MngrDBOperations(Statement st) {
		this.statement = st;
	}

	/**
	 * clean up MySQL
	 * 
	 * @throws SQLException
	 */
	public void deleteAllDB() throws SQLException {
		try {

			// delete everything from both the users and images table in the
			// database
			statement.executeUpdate("DELETE FROM " + GlobalValues.dbTable_Users);
			statement.executeUpdate("DELETE FROM " + GlobalValues.dbTable_Images);

		} finally {
			if (statement != null)
				statement.close();

		}
	}
}