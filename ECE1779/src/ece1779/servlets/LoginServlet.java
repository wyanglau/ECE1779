package ece1779.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ece1779.GlobalValues;
import ece1779.Main;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
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
		if ((testuser.equals(username)) && (testpwd.equals(pwd))) {

			HttpSession session = request.getSession();
			session.setAttribute(GlobalValues.USERNAME, username);
			session.setMaxInactiveInterval(GlobalValues.SESSION_INACTIVE_TIME);

//			Cookie cookie = new Cookie(GlobalValues.USERNAME, username);
//			cookie.setMaxAge(30 * 60);
//
//			response.addCookie(cookie);

			this.setCurrentUser(username, session);

			String encodedURL = response.encodeRedirectURL("../pages/display.jsp");
			response.sendRedirect(encodedURL);

		}

		else {

			response.getWriter().print("Failed to login");
		}

	}

	/**
	 * Including all 1. images/ 2. userid/3. username Find these methods in
	 * ece1779.userOperations.UserOperations
	 * 
	 * @param userName
	 */
	private void setCurrentUser(String userName, HttpSession session) {

		/** TEST DATA **/
		User user = new User(1, userName, null);
		List<Images> imgs = new ArrayList<Images>();
		for (int i = 0; i < 5; i++) {
			String url = "http://img5.duitang.com/uploads/item/201404/04/20140404170308_aCYQr.jpeg";
			Images img = new Images(1, url, url, url, url);
			imgs.add(img);
		}
		user.setImgs(imgs);
		/** TEST DATA END**/
		
		session.setAttribute(GlobalValues.USER_INIT, new Main(user));
	}

}
