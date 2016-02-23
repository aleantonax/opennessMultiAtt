package provider.exceptions;

public class ConnectionFailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionFailException(){
		System.err.println("Connection Fail");
	}
}
