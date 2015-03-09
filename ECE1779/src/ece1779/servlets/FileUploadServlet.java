package ece1779.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
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
import ece1779.commonObjects.User;
import ece1779.userOperations.UserOperations;

/**
 * Servlet implementation class FileUploadServlet
 */
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {

			PrintWriter out = response.getWriter();
			out.write("<br><br><br><br><br><br><br><br><br><br><br><br><div line-height=100px height=100px align=center><h1>Uploading...Hold on...</h1></div>");
			BasicAWSCredentials awsCredentials = (BasicAWSCredentials) this
					.getServletContext().getAttribute(
							GlobalValues.AWS_CREDENTIALS);
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(GlobalValues.CURRENT_USER);
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			List<FileItem> items = upload.parseRequest(request);

			// User ID
			FileItem item1 = (FileItem) items.get(0);

			String userId_key = item1.getFieldName();
			String userId_value = item1.getString();

			if (userId_key.equals("userID") && (!userId_value.equals(""))) {
				user.setId(Integer.parseInt(userId_value));
			}

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
			session.setAttribute(GlobalValues.CURRENT_USER, user);
			System.out.println("[FileUploadServlet] Upload done.");

			RequestDispatcher rd = request
					.getRequestDispatcher("/pages/display.jsp");

			rd.forward(request, response);

		} catch (Exception e) {
			request.setAttribute(GlobalValues.UPLOAD_RESPONSE, false);
			PrintWriter out = response.getWriter();
			out.write(e.getMessage());
			e.printStackTrace();

		} finally {
			// RequestDispatcher rd = request
			// .getRequestDispatcher("/pages/display.jsp");
			//
			// rd.forward(request, response);
		}

	}
}
