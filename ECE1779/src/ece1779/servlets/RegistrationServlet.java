package ece1779.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.Main;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;
import ece1779.loadBalance.CloudWatching;

import java.sql.*;

public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String managerName;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationServlet() {
		super();
	}

	public void init() {
		// Get manager name and password from web.xml
		managerName = this.getServletConfig().getInitParameter("Manager");
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
		// Get username and the 2 typed passwords from the registration form textfields in
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
					try {
						// Create connection to database
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(
								"jdbc:mysql://" + GlobalValues.dbLocation_URL
										+ ":" + GlobalValues.dbLocation_Port
										+ "/" + GlobalValues.dbTable_Users,
								GlobalValues.dbAdmin_Name,
								GlobalValues.dbAdmin_Pass);
						Statement st = con.createStatement();

						// Retrieve information from database with given username
						ResultSet rs;
						rs = st.executeQuery("select * from users where login='" + user
								+ "'");
						
						// if username already exists, registration fails
						if (!userNameTaken (rs))
						{
							// Enter data into database
							int i = st
									.executeUpdate("insert into users(login, password) values ('"
											+ user + "','" + pwd + "')");
	
							// Display success response
							if (i > 0) {
								out.println("Registration is Successful.");
								out.println("<br>");
								out.println("You may now log in.");
								out.println("<br>");
								out.println("<a href='../login.jsp'>Go to Main page</a>");
							} else {
								response.sendRedirect("../login.jsp");
							}
						} else {
							// Username already exists, no data entered
							// into database
							out.println("The username you entered already exists.");
							out.println("<br>");
							out.println("Please try again.");
							out.println("<br>");
							out.println("<a href='../login.jsp'>Go to Main page</a>");
						}
					} catch (SQLException e) {
						System.out
								.println("Connection Failed! Check output console");
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						System.out
								.println("Connection Failed! Check output console");
						e.printStackTrace();
					}
				} else {
					// Password and Password2 did not match, no data entered
					// into database
					out.println("The passwords you entered did not match each other.");
					out.println("<br>");
					out.println("Please try again.");
					out.println("<br>");
					out.println("<a href='../login.jsp'>Go to Main page</a>");
				}
			} else {
				// A field is left empty, no data entered into database
				out.println("The username or password was left empty.");
				out.println("<br>");
				out.println("Please try again.");
				out.println("<br>");
				out.println("<a href='../login.jsp'>Go to Main page</a>");
			}
		} else {
			// username entered was manager's username
			out.println("The username cannot be used");
			out.println("<br>");
			out.println("Please try again.");
			out.println("<br>");
			out.println("<a href='../login.jsp'>Go to Main page</a>");
		}
	}
	
	// checks if username already exists in database
	private boolean userNameTaken (ResultSet rs) {
		try {
			return rs.first();
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}
	}
}
