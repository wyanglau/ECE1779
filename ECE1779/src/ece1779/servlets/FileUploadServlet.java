package ece1779.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		// try {

		// // Create a factory for disk-based file items
		// FileItemFactory factory = new DiskFileItemFactory();
		//
		// // Create a new file upload handler
		// ServletFileUpload upload = new ServletFileUpload(factory);
		//
		// // Parse the request
		// List /* FileItem */ items = upload.parseRequest(request);
		//
		//
		// // User ID
		// FileItem item1 = (FileItem)items.get(0);
		//
		// String name = item1.getFieldName();
		// String value = item1.getString();
		//
		// //Uploaded File
		// FileItem theFile = (FileItem)items.get(1);
		//
		//
		// // filename on the client
		// String fileName = theFile.getName();
		//
		// // get root directory of web application
		// String path = this.getServletContext().getRealPath("/");
		//
		//
		// String key1 = "MyObjectKey_" + UUID.randomUUID();
		// String key2 = "MyObjectKey_" + UUID.randomUUID();
		//
		//
		// String name1 = path+key1;
		// String name2 = path+key2;
		//
		// // store file in server
		// File file1 = new File(name1);
		// theFile.write(file1);
		//
		// // Use imagemagik to transform image
		// IMOperation op = new IMOperation();
		// op.addImage();
		// op.flip();
		// op.addImage();
		//
		// ConvertCmd cmd = new ConvertCmd();
		// cmd.run(op, name1,name2);
		//
		// File file2 = new File(name2);
		//
		//
		//
		//
		// PrintWriter out = response.getWriter();
		//
		//
		// s3SaveFile(file1, key1, out);
		// s3SaveFile(file2, key2, out);
		//
		// updateDatabase(key1,out);
		//
		//
		// response.setContentType("text/html");
		//
		// out.write("<html><head><title>Sample Image Upload</title></head>");
		// out.write("<body>");
		//
		// out.write("<img src='https://s3.amazonaws.com/ece1779winter2013/" +
		// key1 + "' />");
		// out.write("<img src='https://s3.amazonaws.com/ece1779winter2013/" +
		// key2 + "' />");
		//
		// out.write("</body></html>");
		//
		// }
	}
}
