package ece1779;

public class GlobalValues {
	/**
	 * KEY of class Main for Servlet Initialization Context
	 */
	public static final String USER_INIT = "init";

	public static final String USERNAME = "user";

	public static final String PASSWORD = "password";

	/**
	 * Session inactive time = 30
	 */
	public static final int SESSION_INACTIVE_TIME = 30;

	public static final String AWS_CREDENTIALS = "AWScredentials";

	/**
	 * S3 bucket name
	 */
	public static String BUCKET_NAME = "ece1779winter2015group14number1";

	/**
	 * S3 bucket endpoint, we can access an image key=sample_123456 by
	 * 
	 * http://ece1779winter2015group14number1.s3-website-us-west-2.amazonaws.com
	 * /sample_123456
	 */
	public static String BUCKET_ENDPOINT = "http://ece1779winter2015group14number1.s3-website-us-west-2.amazonaws.com/";

	/**
	 * ECE1779_GROUP14
	 */
	public static String SECURITY_GROUP_ID = "sg-36c76d52";

	public static String AMI_ID = "ami-06efba6e";

}
