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

import java.sql.*;

public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet() {
        super();
    }

	public void init() {
		
		
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse 
	 * 		response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse 
	 * 		response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String user = (String) request.getParameter(GlobalValues.USERNAME);
		String pwd = (String) request.getParameter(GlobalValues.PASSWORD);
		String pwd2 = (String) request.getParameter(GlobalValues.PASSWORD2);
		

	    if (user.length() != 0 && pwd.length() != 0 && pwd2.length() != 0)
	    {
		    if (pwd.compareTo(pwd2) == 0)
		    {
		    	try{
			    	Class.forName("com.mysql.jdbc.Driver");
			    	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbUsers",
			            "root", "qwer1234");
			    	Statement st = con.createStatement();
			    	//ResultSet rs;
			    	int i = st.executeUpdate("insert into users(login, password) values ('" + user + "','" + pwd + "')");
			
			    	if (i > 0) {
			        	System.out.println("Registration is Successful.");
			        	//out.println("<br>");
			        	//out.println("You may now log in.");
			            //out.println("<br>"); 
			            //out.println("<a href='index.jsp'>Go to Main page</a>");
			    	} else {
			        	response.sendRedirect("login.jsp");
			    	}
		    	}
		    	catch (SQLException e) {
		            System.out.println("Connection Failed! Check output console");
		            e.printStackTrace();
		        }
		    }
		    else {
		        System.out.println("The passwords you entered did not match each other.");
		        //out.println("<br>");
		        //out.println("Please try again.");
		        //out.println("<br>"); 
		        //out.println("<a href='index.jsp'>Go to Main page</a>");
		    }
	    }
	    else {
	        System.out.println("The username or password was left empty.");
	        //out.println("<br>");
	        //out.println("Please try again.");
	        //out.println("<br>"); 
	        //out.println("<a href='index.jsp'>Go to Main page</a>");
	    }
	}

}








