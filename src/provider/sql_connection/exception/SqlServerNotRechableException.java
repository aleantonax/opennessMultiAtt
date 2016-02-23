package provider.sql_connection.exception;


public class SqlServerNotRechableException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7058703093924185255L;
	public SqlServerNotRechableException(){
		super();
	}
	public SqlServerNotRechableException(String message){
		super(message);
	}
}
