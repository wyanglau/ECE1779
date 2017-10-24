package ece1779.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import ece1779.GlobalValues;
import ece1779.commonObjects.User;
import ece1779.DAO.*;

import java.sql.*;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SharedPoolDataSource dbcp;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	public void init() {
		dbcp = (SharedPoolDataSource) this.getServletContext().getAttribute(
				GlobalValues.Connection_Tag);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Get username and password from the login form textfields in
		// ../login.jsp

		System.out.println("[LoginServlet] Begin to login.");
		String user = (String) request.getParameter(GlobalValues.USERNAME);
		String pwd = (String) request.getParameter(GlobalValues.PASSWORD);
		System.out.println("[LoginServlet] Input user:" + user + " Password:"
				+ pwd);
		// Setup user object
		User currentUser = new User(-1, user, null);

		// PrintWriter used to make response messages below
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// if password entered was blank, don't check anything; it was wrong
		if (pwd.length() > 0) {
			if (userExists(currentUser)) {
				// check for normal user login, whether password matches
				if (userPasswordMatches(currentUser, pwd)) {
					HttpSession session = request.getSession();
					sessionAndCookieSetup(user, request, response, session);
					this.setCurrentUser(GlobalValues.PRIVILEGE_ADMIN,
							currentUser, session);
					String encodedURL = response
							.encodeRedirectURL("../pages/display.jsp");
					response.sendRedirect(encodedURL);
				} else {
					// password entered was wrong
					out.println("Invalid login information. <a href='../login.jsp'>Try again</a>.");
				}
			}
			// neither manage nor user information was correctly given
			else {
				out.println("Invalid login information. <a href='../login.jsp'>Try again</a>.");
			}
		}
		// password entered was blank
		else {
			out.println("Invalid login information. <a href='../login.jsp'>Try again</a>.");
		}
	}

	// checks if user exists in database
	private boolean userExists(User currentUser) {
		System.out.println("[LoginServlet] checking does user exist.");
		try {
			UserDBOperations udbo = new UserDBOperations(currentUser, dbcp);
			int tempInt = udbo.findUserID();
			if (tempInt >= 0) {
				currentUser.setId(tempInt);
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}
	}

	// checks if entered info matches user password in database
	private boolean userPasswordMatches(User currentUser, String password) {
		System.out.println("[LoginServlet]Checking if it is authentic user");
		try {
			UserDBOperations udbo = new UserDBOperations(currentUser, dbcp);
			return (udbo.findUserPW().compareTo(password) == 0 ? true : false);

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * after checking users, set session and cookie
	 * 
	 * @param user
	 *            - username
	 * @param request
	 *            - servlet request
	 * @param response
	 *            - servlet response
	 * @param session
	 *            - current servlet session
	 */
	private void sessionAndCookieSetup(String user, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		session.setAttribute(GlobalValues.USERNAME, user);
		session.setMaxInactiveInterval(GlobalValues.SESSION_INACTIVE_TIME);
		Cookie cookie = new Cookie(GlobalValues.USERNAME, user);
		cookie.setMaxAge(GlobalValues.SESSION_INACTIVE_TIME);
		response.addCookie(cookie);
	}

	/**
	 * Including all 1. images/ 2. userid/3. username Find these methods in
	 * ece1779.userOperations.UserOperations
	 * 
	 * @param privilege
	 * @param userName
	 * @param userID
	 * @param session
	 * 
	 */
	private void setCurrentUser(String privilege, User user, HttpSession session) {// Load
																					// image
																					// set
																					// of
																					// the
																					// current
																					// user
		UserDBOperations udbo = new UserDBOperations(user, dbcp);

		// manager has userID -2, and does not have an image set
		// do not try to retrieve image set if manager is logged in
		if (user.getId() >= 0) {
			// set user's img set to the above
			try {
				user.setImgs(udbo.findAllImgs());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// set user attributes for both Users and Admin
		session.setAttribute(GlobalValues.CURRENT_USER, user);
		session.setAttribute(GlobalValues.PRIVILEGE_TAG, privilege);

		// test output -> give all keys of current img set
		// System.out.println(user.getImgs().get(0).getKeys().get(1));

	}
}
