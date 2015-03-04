package ece1779.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.loadBalance.WorkerPoolManagement;

/**
 * Servlet implementation class ManualShrinkServlet
 */
public class ManualShrinkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManualShrinkServlet() {
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
		try {

			System.out.println("Shrinking Worker Pool");
			BasicAWSCredentials awsCredentials = (BasicAWSCredentials) this
					.getServletContext().getAttribute(
							GlobalValues.AWS_CREDENTIALS);
			int ratio = Integer.parseInt((String) request
					.getParameter("manualShrinkRatio"));

			if (ratio == 0) {
				System.out.println("Invalid Ratio Parameter");
				return;
			}
			WorkerPoolManagement wpm = new WorkerPoolManagement(awsCredentials);
			wpm.shrinking(ratio);

		} catch (Exception e) {
			// to do
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

	}

}
