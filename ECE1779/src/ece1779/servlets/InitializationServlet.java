package ece1779.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.Main;
import ece1779.commonObjects.Images;
import ece1779.commonObjects.User;

public class InitializationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitializationServlet() {
		super();
	}

	public void init(ServletConfig config) {

		initAWS(config);
		initJDBC();

		// to-do get current user

		// This is the test data, you should get user from the session
		User user = new User(1, "Ryan", null);
		List<Images> imgs = new ArrayList<Images>();
		for (int i = 0; i < 5; i++) {
			String url = "http://img5.duitang.com/uploads/item/201404/04/20140404170308_aCYQr.jpeg";
			Images img = new Images(1, url, url, url, url);
			imgs.add(img);
		}
		user.setImgs(imgs);
		// Test data END

		this.getServletContext().setAttribute(GlobalValues.USER_INIT,
				new Main(user));

	}

	private void initAWS(ServletConfig config) {

		String accessKey = config.getInitParameter("AWSaccessKey");
		String secretKey = config.getInitParameter("AWSsecretKey");
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);

		config.getServletContext().setAttribute("AWScredentials",
				awsCredentials);
	}

	private void initJDBC() {

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
		// TODO Auto-generated method stub
	}

}
