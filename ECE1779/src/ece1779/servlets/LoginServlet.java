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
import javax.servlet.http.Cookie;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.Main;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;
import ece1779.loadBalance.CloudWatching;

import java.sql.*;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String managerName;
	private String managerPassword;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	public void init() {
		managerName = this.getServletConfig().getInitParameter("Manager");
		managerPassword = this.getServletConfig().getInitParameter(
				"ManagerPassword");
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
		// Get username and password from the login form textfields in
		// ../login.jsp
		String user = (String) request.getParameter(GlobalValues.USERNAME);
		String pwd = (String) request.getParameter(GlobalValues.PASSWORD);

		// PrintWriter used to make response messages below
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// Create connection to database
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://"
					+ GlobalValues.dbLocation_URL + ":"
					+ GlobalValues.dbLocation_Port + "/"
					+ GlobalValues.dbTable_Users, GlobalValues.dbAdmin_Name,
					GlobalValues.dbAdmin_Pass);
			Statement st = con.createStatement();
			ResultSet rs;
			// Retrieve information from database with given login info
			rs = st.executeQuery("select * from users where login='" + user
					+ "' and password='" + pwd + "'");

			// check first to see if it is the manager logging in
			if (isManager(user, pwd)) {
				HttpSession session = request.getSession();
				sessionAndCookieSetup(user, request, response, session);
				// this.systemSetup(GlobalValues.PRIVILEGE_ADMIN, user, session);
				// UserSetup();
				String encodedURL = response
						.encodeRedirectURL("../pages/managerView.jsp");
				response.sendRedirect(encodedURL);
			}
			// check for normal user login
			else if (userExists(rs)) {
				HttpSession session = request.getSession();
				sessionAndCookieSetup(user, request, response, session);
				// this.systemSetup(GlobalValues.PRIVILEGE_USER, user, session);
				// UserSetup();
				String encodedURL = response
						.encodeRedirectURL("../pages/display.jsp");
				response.sendRedirect(encodedURL);
			} 
			// neither manage nor user information was correctly given
			else {
				out.println("Invalid login informatin. <a href='../login.jsp'>Try again</a>.");
			}
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
	}
		
	// checks if user exists in database
	private boolean userExists (ResultSet rs) {
		try {
			return rs.first();
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}
	}

	// checks if login information corresponds to manager
	private boolean isManager(String username, String password) {
		if (username.equals(managerName) && password.equals(managerPassword)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * after checking users, set session and cookie
	 * 
	 * @param user - username
	 * @param request - servlet request
	 * @param response - servlet response 
	 * @param session - current servlet session
	 */
	private void sessionAndCookieSetup(String user, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		session.setAttribute(GlobalValues.USERNAME, user);
		session.setMaxInactiveInterval(GlobalValues.SESSION_INACTIVE_TIME );
		Cookie cookie = new Cookie(GlobalValues.USERNAME, user);
		cookie.setMaxAge(GlobalValues.SESSION_INACTIVE_TIME);
		response.addCookie(cookie);
	}
	
	/**
	 * setup the AWS system
	 * 
	 * @param privilege
	 * @param userName
	 * @param session
	 */
	private void systemSetup(String privilege, String userName,
			HttpSession session) {
		// set up user
		this.setCurrentUser(privilege, userName, session);

		// set up cloudwatching
		ServletContext context = this.getServletContext();
		BasicAWSCredentials credentials = (BasicAWSCredentials) context
				.getAttribute(GlobalValues.AWS_CREDENTIALS);
		CloudWatching cw = new CloudWatching(credentials);
		session.setAttribute(GlobalValues.CLOUD_WATCHING, cw);
	}

	/**
	 * Including all 1. images/ 2. userid/3. username Find these methods in
	 * ece1779.userOperations.UserOperations
	 * 
	 * @param userName
	 */
	private void setCurrentUser(String privilege, String userName,
			HttpSession session) {

		/** TEST DATA **/
		User user = new User(1, userName, null);
		List<Images> imgs = new ArrayList<Images>();
		for (int i = 0; i < 5; i++) {
			String url = "https://s3-us-west-2.amazonaws.com/ece1779winter2015group14number1/MyObjectKey_d6310487-f3aa-4c62-81ab-79748b8975fa";
			List<String> keys = new ArrayList<String>();
			keys.add(url);
			keys.add(url);
			keys.add(url);
			Images img = new Images(1, 1, keys);
			imgs.add(img);
		}
		user.setImgs(imgs);
		/** TEST DATA END **/
		session.setAttribute(GlobalValues.USER_INIT, new Main(user));
		session.setAttribute(GlobalValues.PRIVILEGE_TAG, privilege);
	}

}
