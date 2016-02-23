package provider;

import provider.exceptions.ConnectionFailException;
import provider.exceptions.UnavailableServerException;

/**
 * Represent the interface to implement the Factory Pattern.
 * 
 * @author Iaffaldano Giuseppe M.587556
 * @author Cavallo Giacomo M.588009
 * 
 */
public interface Factory {

	public DataServer getServer(String about)
			throws UnavailableServerException, ConnectionFailException;

}
