package ece1779.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import ece1779.GlobalValues;
import ece1779.DAO.UserDBOperations;
import ece1779.commonObjects.User;

import java.sql.*;

public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String managerName;

	private Connection con = null;
	private Statement st = null;
	private SharedPoolDataSource dbcp;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationServlet() {
		super();
	}

	public void init() {
		// Get manager name and password from web.xml
		managerName = this.getServletConfig().getInitParameter("Manager");
		dbcp = (SharedPoolDataSource) this.getServletContext().getAttribute(
				GlobalValues.Connection_Tag);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Get username and the 2 typed passwords from the registration form
		// textfields in
		// login.jsp
		String user = (String) request.getParameter(GlobalValues.regUSERNAME);
		String pwd = (String) request.getParameter(GlobalValues.regPASSWORD);
		String pwd2 = (String) request.getParameter(GlobalValues.regPASSWORD2);

		// PrintWriter used to make response messages below
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// username entered cannot match manager username
		if (user.compareToIgnoreCase(managerName) != 0) {
			// username and password entries cannot be empty
			if (user.length() != 0 && pwd.length() != 0 && pwd2.length() != 0) {
				// password and repeated password must match
				if (pwd.compareTo(pwd2) == 0) {
					// Setup user object
					User currentUser = new User(-1, user, null);

					// check if username available
					// if it is not, then registration fails
					if (userNameAvailable(currentUser)) {
						// Register data into database
						// regUser(User, pwd) returns boolean TRUE for success,
						// FALSE for failure
						boolean regSuccess = regUser(currentUser, pwd);

						// resolve registration success visually
						if (regSuccess) {
							// Display success response
							createSuccessPage(out,
									"Registration is Successful.");
						} else {
							// Display fail response
							createErrorPage(out, "Registration failed.");
						}
					}
					// Username already exists, no data entered
					// into database
					else {
						createErrorPage(out, "Username already exists");
					}
				} else {
					// Password and Password2 did not match, no data entered
					// into database
					createErrorPage(out,
							"The passwords you entered did not match each other.");
				}
			} else {
				// A field is left empty, no data entered into database
				createErrorPage(out, "The username or password was left empty.");
			}
		} else {
			// username entered was manager's username
			createErrorPage(out, "The username cannot be used");
		}
	}

	// creates the error page and redirect back to main page
	private void createErrorPage(PrintWriter out, String reason) {
		out.println(reason);
		out.println("<br>");
		out.println("Please try again.");
		out.println("<br>");
		out.println("<a href='../login.jsp'>Go to Main page</a>");
	}

	// creates the success page and redirect back to main page
	private void createSuccessPage(PrintWriter out, String reason) {
		out.println(reason);
		out.println("<br>");
		out.println("You may now log in.");
		out.println("<br>");
		out.println("<a href='../login.jsp'>Go to Main page</a>");
	}

	// checks if username exists in database
	private boolean userNameAvailable(User username) {
		try {
			UserDBOperations udbo = new UserDBOperations(username, dbcp);

			return (udbo.findUserID() == -1 ? true : false);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return true;
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException logOrIgnore) {
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException logOrIgnore) {
				}
		}
	}

	/**
	 * Actual procedure to add User to database
	 */
	private boolean regUser(User username, String pwd) {
		try {
			con = ((DataSource) this.getServletContext().getAttribute(
					GlobalValues.Connection_Tag)).getConnection();
			st = con.createStatement();
			UserDBOperations udbo = new UserDBOperations(username, dbcp);

			boolean regResult;

			// Call userDBops addUser method
			// attempt to register the user
			regResult = udbo.addUser(pwd);

			return regResult;
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException logOrIgnore) {
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException logOrIgnore) {
				}
		}
	}
}
