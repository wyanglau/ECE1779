package ece1779.servlets;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;

import java.sql.*;

import org.apache.commons.dbcp.cpdsadapter.*;
import org.apache.commons.dbcp.datasources.*;

public class InitializationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitializationServlet() {
		super();
	}
	
	public void init(){
		initAWS(this.getServletConfig());
		initJDBC();
	}


	private void initAWS(ServletConfig config) {

		String accessKey = config.getInitParameter("AWSaccessKey");
		String secretKey = config.getInitParameter("AWSsecretKey");
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		config.getServletContext().setAttribute(GlobalValues.AWS_CREDENTIALS,
				awsCredentials);
	}

	private void initJDBC() {
		try {  
		    //Initialize connection pool    

		    getServletContext().log("SQLGatewayPool: Connecting to DB");

			   
		    DriverAdapterCPDS ds = new DriverAdapterCPDS();
		    ds.setDriver("com.mysql.jdbc.Driver");
		    ds.setUrl("jdbc:mysql://"
					+ GlobalValues.dbLocation_URL + ":"
					+ GlobalValues.dbLocation_Port + "/"
					+ GlobalValues.dbLocation_Schema);
		    
		    ds.setUser(GlobalValues.dbAdmin_Name);
		    ds.setPassword(GlobalValues.dbAdmin_Pass);

		    SharedPoolDataSource dbcp = new SharedPoolDataSource();
		    dbcp.setConnectionPoolDataSource(ds);
		    
		    Connection con = dbcp.getConnection();
	
		    Statement st = con.createStatement();
			
			this.getServletContext().setAttribute(GlobalValues.ConnectionStatement_Tag, st);
		}
		catch (Exception ex) {
		    getServletContext().log("SQLGatewayPool Error: " + ex.getMessage());
		    ex.printStackTrace();
		}
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
