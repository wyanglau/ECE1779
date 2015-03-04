package ece1779.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.loadBalance.LoadBalancerOperation;
import ece1779.loadBalance.WorkerPoolManagement;

/**
 * Servlet implementation class ManualGrowServlet
 */
public class ManualGrowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManualGrowServlet() {
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
			BasicAWSCredentials awsCredentials = (BasicAWSCredentials) this
					.getServletContext().getAttribute(
							GlobalValues.AWS_CREDENTIALS);
			int ratio = Integer.parseInt((String) request.getParameter("manualGrowRatio"));
			if(ratio ==0){
				System.out.println("Invalid Ratio Parameter");
				return;
			}
			WorkerPoolManagement wpm = new WorkerPoolManagement(awsCredentials);

			System.out.println("Expanding Worker Pool");
			wpm.growingByRatio(ratio);

		} catch (Exception e) {

			// to do ...exception handling
			e.printStackTrace();
		}

	}

}
