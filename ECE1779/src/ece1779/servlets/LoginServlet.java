package ece1779.servlets;

import java.io.IOException;
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
		String testuser = "ryan";
		String testpwd = "123";

		String username = (String) request.getParameter(GlobalValues.USERNAME);
		String pwd = (String) request.getParameter(GlobalValues.PASSWORD);
		if (isManager(username, pwd)) {
			HttpSession session = request.getSession();
			session.setAttribute(GlobalValues.USERNAME, username);
			session.setMaxInactiveInterval(GlobalValues.SESSION_INACTIVE_TIME);
			session.setMaxInactiveInterval(30 * 60);
			// Cookie cookie = new Cookie(GlobalValues.USERNAME, username);
			// cookie.setMaxAge(30 * 60);
			//
			// response.addCookie(cookie);

			this.systemSetup(GlobalValues.PRIVILEGE_ADMIN, username, session);

			String encodedURL = response
					.encodeRedirectURL("../pages/managerView.jsp");
			response.sendRedirect(encodedURL);

		}
		if ((testuser.equals(username)) && (testpwd.equals(pwd))) {

			HttpSession session = request.getSession();
			session.setAttribute(GlobalValues.USERNAME, username);
			session.setMaxInactiveInterval(GlobalValues.SESSION_INACTIVE_TIME);

			// Cookie cookie = new Cookie(GlobalValues.USERNAME, username);
			// cookie.setMaxAge(30 * 60);
			//
			// response.addCookie(cookie);

			this.systemSetup(GlobalValues.PRIVILEGE_USER, username, session);

			String encodedURL = response
					.encodeRedirectURL("../pages/display.jsp");
			response.sendRedirect(encodedURL);

		}

		else {

			response.getWriter().print("Failed to login");
		}

	}

	private boolean isManager(String username, String password) {
		if (username.equals(managerName) && password.equals(managerPassword)) {
			return true;
		} else {
			return false;
		}
	}

	private void systemSetup(String privilege, String userName, HttpSession session) {
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
