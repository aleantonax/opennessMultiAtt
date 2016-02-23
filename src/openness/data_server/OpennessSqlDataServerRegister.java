package openness.data_server;

import provider.DataServer;
import provider.exceptions.ServerAlreadyExistException;
import provider.exceptions.UnavailableServerException;
import provider.registration.DataServerRegistration;
import provider.registration.ServerRegistration;

/**
 * Allows to register the LocalDataServer
 * 
 * @author Francesco Centrone 608758
 *
 */
public class OpennessSqlDataServerRegister extends DataServerRegistration {
	public static final String SERVER_NAME = "OPENNESSSQL";
	public static DataServer server = new OpennessSqlDataServer();

	/**
	 * Inserts DataServer to the Register
	 * 
	 * @throws ServerAlreadyExistException
	 */
	public void logIN() throws ServerAlreadyExistException {
		ServerRegistration.logIN(SERVER_NAME, server);
	}

	/**
	 * Removes DataServer from the Register
	 * 
	 * @throws UnavailableServerException
	 */
	public void logOUT() throws UnavailableServerException {
		ServerRegistration.logOUT(SERVER_NAME);
	}

}
