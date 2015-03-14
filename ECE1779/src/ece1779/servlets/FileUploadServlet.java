package ece1779.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.DAO.UserDBOperations;
import ece1779.commonObjects.User;
import ece1779.userOperations.UserOperations;

/**
 * Servlet implementation class FileUploadServlet
 */
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private User user;
	private BasicAWSCredentials awsCredentials;
	private SharedPoolDataSource dbcp;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploadServlet() {
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
	 * This method is only used to meet the requirment of
	 * LoadGenerator.........cuz it invoke our servlet directly, regardless of
	 * our initialization......
	 * 
	 * @throws SQLException
	 */
	private void setInitialData(HttpSession session) throws SQLException {
		ServletConfig config = this.getServletConfig();

		if (session == null) {
			String accessKey = config.getInitParameter("AWSaccessKey");
			String secretKey = config.getInitParameter("AWSsecretKey");
			this.awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

			this.user = new User(-1, "LoadGenerator", null);
			UserDBOperations udbo = new UserDBOperations(user, this.dbcp);
			int tempInt = udbo.findUserID();
			System.out
					.println("[UploadServlet] Searching for user , returning : "
							+ tempInt);
			if (tempInt >= 0) {
				user.setId(tempInt);
			} else {
				System.out
						.println("[UploadServlet] Registration for a new user.");
				udbo.addUser("123");
				user.setId(udbo.findUserID());
			}
		} else {
			this.awsCredentials = (BasicAWSCredentials) this
					.getServletContext().getAttribute(
							GlobalValues.AWS_CREDENTIALS);
			// if (awsCredentials == null) {
			// String accessKey = config.getInitParameter("AWSaccessKey");
			// String secretKey = config.getInitParameter("AWSsecretKey");
			// this.awsCredentials = new BasicAWSCredentials(accessKey,
			// secretKey);
			// }
			this.user = (User) session.getAttribute(GlobalValues.CURRENT_USER);

			// if (user == null) {
			// this.user = new User(-1, "LoadGenerator", null);
			// UserDBOperations udbo = new UserDBOperations(user, this.dbcp);
			// int tempInt = udbo.findUserID();
			// System.out
			// .println("[UploadServlet] Searching for user , returning : "
			// + tempInt);
			// if (tempInt >= 0) {
			// user.setId(tempInt);
			// } else {
			// System.out
			// .println("[UploadServlet] Registration for a new user.");
			// udbo.addUser("123");
			// user.setId(udbo.findUserID());
			// }
		}
		System.out.println("[UploadServlet] Current user : [ "
				+ user.getUserName() + " ], userID:[" + user.getId() + "].");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			this.dbcp = (SharedPoolDataSource) this.getServletContext()
					.getAttribute(GlobalValues.Connection_Tag);

			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write("<br><br><br><br><br><br><br><br><br><br><br><br><div line-height=100px height=100px align=center><h1>Uploading...Hold on...</h1></div>");
			HttpSession session = request.getSession(false);

			this.setInitialData(session);
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			List<FileItem> items = upload.parseRequest(request);

			/**
			 * FYI: this id is used for loadgenerator....but the sql constrain
			 * forbid us to complete insert of invalid user id... Codes leave
			 * here just in case we will get the super privilege.
			 */
			// User ID
			// FileItem item1 = (FileItem) items.get(0);
			// String userId_key = item1.getFieldName();
			// String userId_value = item1.getString();
			// if (userId_key.equals("userID") &&
			// (!userId_value.equals(""))) {
			// user.setId(Integer.parseInt(userId_value));
			// }

			// Uploaded File
			FileItem theFile = (FileItem) items.get(1);

			// filename on the client
			// String fileName = theFile.getName();

			// get root directory of web application
			String path = this.getServletContext().getRealPath("/");

			String newFilePath = path + UUID.randomUUID();

			// store file in server
			File fileToBeStored = new File(newFilePath);
			theFile.write(fileToBeStored);

			SharedPoolDataSource dbcp = (SharedPoolDataSource) this
					.getServletContext().getAttribute(
							GlobalValues.Connection_Tag);
			UserOperations uo = new UserOperations(user, awsCredentials, dbcp);
			uo.uploadAndSave(fileToBeStored);

			request.setAttribute(GlobalValues.UPLOAD_RESPONSE, true);
			if (session != null) {
				session.setAttribute(GlobalValues.CURRENT_USER, user);
			}
			System.out.println("[FileUploadServlet] Upload done.");
		} catch (Exception e) {
			request.setAttribute(GlobalValues.UPLOAD_RESPONSE, false);
			PrintWriter out = response.getWriter();
			out.write(e.toString());
			e.printStackTrace();

		} finally {
			if (!this.user.getUserName().contains("LoadGenerator")) {
				RequestDispatcher rd = request
						.getRequestDispatcher("/pages/display.jsp");
				if (rd != null) {
					rd.forward(request, response);
				}
			}
		}

	}
}
