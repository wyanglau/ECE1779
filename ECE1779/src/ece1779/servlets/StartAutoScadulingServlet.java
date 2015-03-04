package ece1779.servlets;

import java.io.IOException;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.loadBalance.AutoScaling;

/**
 * Servlet implementation class AutoScaduling
 */
public class StartAutoScadulingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartAutoScadulingServlet() {
		super();
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

		try {

			BasicAWSCredentials awsCredentials = (BasicAWSCredentials) this
					.getServletContext().getAttribute(
							GlobalValues.AWS_CREDENTIALS);

			String expandThreshlod = (String) request
					.getParameter("expandThreshlod");
			String shrinkThreshlod = (String) request
					.getParameter("shrinkThreshlod");

			String growRatio = (String) request.getParameter("growRatio");
			String shrinkRatio = (String) request.getParameter("shrinkRatio");
			System.out
					.println("[StartAutoScadulingServlet] Get parameters from request : [expandThreshlod:"
							+ expandThreshlod
							+ "][shrinkThreshlod:"
							+ shrinkThreshlod
							+ "][growRatio:"
							+ growRatio
							+ "][shrinkRatio:" + shrinkRatio + "]");
			Timer timer = new Timer();
			long start = System.currentTimeMillis();
			System.out
					.println("[StartAutoScadulingServlet] scheduling new timer, current time is "
							+ start);
			timer.schedule(
					new AutoScaling(awsCredentials, Double
							.parseDouble(expandThreshlod), Double
							.parseDouble(shrinkThreshlod), Integer
							.parseInt(growRatio), Integer.parseInt(shrinkRatio)),
					0, 5 * 60 * 1000); // 5mins
			this.getServletContext().setAttribute("Timer", timer);
		} catch (Exception e) {
			// to do : handle the aws exceptions
			e.printStackTrace();
		}

	}

}
