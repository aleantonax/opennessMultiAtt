package provider.exceptions;

public class UnavailableServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnavailableServerException(){
		System.err.println("The spacifie server isn't linked to the register or it is unavailable");
	}
	
}
