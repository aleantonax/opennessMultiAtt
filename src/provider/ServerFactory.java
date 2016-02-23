package provider;

import provider.exceptions.ConnectionFailException;
import provider.exceptions.ConnectionFaitException;
import provider.exceptions.UnavailableServerException;
import provider.registration.ServerRegistration;

/**
 * Rappresent the implementation of Factory Pattern, usefully to select The
 * DataServer.
 * 
 * @author Iaffaldano Giuseppe M.587556
 * @author Cavallo Giacomo M.588009
 * 
 */
public class ServerFactory implements Factory {

	/**
	 * This method allows to select the correct DataServe based on configuration
	 * String.
	 * 
	 * @param about
	 *            - Represent the configuration string.
	 * @return the DataSercer.
	 * @throws UnavailableServerException
	 * @throws ConnectionFaitException
	 */
	@Override
	public DataServer getServer(String about)
			throws UnavailableServerException, ConnectionFailException {
		assert (!about.equals("")) : "Invalid parameter \"about\"";

		return ServerRegistration.getServer(about);
	}

}
