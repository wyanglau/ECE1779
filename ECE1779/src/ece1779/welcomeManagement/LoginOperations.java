package ece1779.welcomeManagement;


public class LoginOperations {

	/**
	 * Save New Users
	 * @return True/False
	 */
	public boolean save(String userName,String password) {
		
		if(isUserExisted(userName))
		{
			return false;
		}
		
		
		/**
		 *  Call SAVE method in db, to be done.
		 */
		
		return false;
	}


	/**
	 * check if it is authentic user
	 * @param name
	 * @param password
	 * @return
	 */
	public boolean loginVerification(String name, String password){
		
		/**
		 * 1. check if it is Manager, if is, call managerVerification
		 *  
		 * 2. if it is User, verify the authentication of user
		 *
		 */
		
		return false;
	}
	
	private boolean managerVerification(String name, String password){
		return false;
	}
	
	/**
	 *  check 
	 *  1 . if userName already existed in DB
	 *  2 . if userName the same as Manager's
	 *  
	 * @param userName
	 * @return
	 */
	private boolean isUserExisted(String userName){
		
		return false;
	}

}
