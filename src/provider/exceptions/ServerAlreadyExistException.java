package provider.exceptions;

public class ServerAlreadyExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerAlreadyExistException(){
		System.err.println("The Server identification is already present into the register");
	}
}
