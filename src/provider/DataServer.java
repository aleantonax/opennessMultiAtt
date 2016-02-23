package provider;

import provider.exceptions.ConnectionFailException;
import provider.exceptions.ConnectionFaitException;
import provider.resourceBuilder.FifResource;

/**
 * Represent the structure of the DataServer. Each DataServer must extend this
 * abstract class with his method to be compatible whit Fif-Provider.
 * 
 * @author Iaffaldano Giuseppe M.587556
 * @author Cavallo Giacomo M.588009
 * 
 */
public abstract class DataServer {

	/**
	 * This method checks if there is other element in the DataServer.
	 * 
	 * @return true if there is other element, false otherwise.
	 */
	public abstract boolean hasNext();

	/**
	 * This method takes the next element in the DataServer.
	 * 
	 * @return the next element in the DataServer.
	 */
	public abstract FifResource next();

	/**
	 * This method is useful if the DataServer need to be configured.
	 * 
	 * @param info
	 *            - represent the configuration string, the structure of the
	 *            string depend on the DataServer.
	 * @throws ConnectionFaitException 
	 */
	public abstract void configureServer(String info) throws ConnectionFailException;

}
