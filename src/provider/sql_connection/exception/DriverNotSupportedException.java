package provider.sql_connection.exception;


public class DriverNotSupportedException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DriverNotSupportedException(){
		super();
	}
	public DriverNotSupportedException(String message){
		super(message);
	}
}
