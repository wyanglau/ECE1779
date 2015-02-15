package ece1779.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

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

		// // to-do get current user
		//
		// // This is the test data, you should get user from the session
		// User user = new User(1, "Ryan", null);
		// List<Images> imgs = new ArrayList<Images>();
		// for (int i = 0; i < 5; i++) {
		// String url =
		// "https://s3-us-west-2.amazonaws.com/ece1779winter2015group14number1/MyObjectKey_d6310487-f3aa-4c62-81ab-79748b8975fa";
		// List<String> keys = new ArrayList<String>();
		// keys.add(url);
		// keys.add(url);
		// keys.add(url);
		// Images img = new Images(1, keys);
		// imgs.add(img);
		// }
		// user.setImgs(imgs);
		// // Test data END
		//
		// this.getServletContext().setAttribute(GlobalValues.USER_INIT,
		// new Main(user));

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
