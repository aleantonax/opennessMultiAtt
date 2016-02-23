package provider.registration;

import java.util.HashMap;

import provider.DataServer;
import provider.exceptions.ConnectionFailException;
import provider.exceptions.ConnectionFaitException;
import provider.exceptions.ServerAlreadyExistException;
import provider.exceptions.UnavailableServerException;

/**
 * Represent the manager of DataServer Registration.
 * 
 * @author Iaffaldano Giuseppe M.587556
 * @author Cavallo Giacomo M.588009
 * 
 */
public class ServerRegistration {

	/**
	 * Is the Dictionary that contain the DataServer. The Key is the unique
	 * identifier of the DataServer and the element is the DataServer.
	 */
	private static HashMap<String, DataServer> dataServer = new HashMap<String, DataServer>();

	/**
	 * The method provide to search the DataServer into the Dictionary.
	 * 
	 * @param about
	 *            - Represent the Server's configuration String.
	 * @return DataServer if it is present in the Dictionary.
	 * @return null.
	 * @throws UnavailableServerException
	 * @throws ConnectionFaitException
	 */

	// restituisce il dataserver corrispondente alla stringa di configurazione
	// data in input
	public static DataServer getServer(String about) throws UnavailableServerException, ConnectionFailException {
		assert (!about.equals("")) : "Invalid parameter \"about\"";

		String[] conf = about.split("::");
		String source = conf[0];
		String infoConf = conf[1];
		if (dataServer.containsKey(source)) {
			DataServer temp = dataServer.get(source);
			temp.configureServer(infoConf);
			return temp;
		}
		throw new UnavailableServerException();
	}

	/**
	 * This method insert the DataServer in the Dictionary, if it isn't present
	 * yet.
	 * 
	 * @param name
	 *            - identifier of DataServer
	 * @param server
	 *            - DataServer
	 * @throws ServerAlreadyExistException
	 */
	public static void logIN(String name, DataServer server) throws ServerAlreadyExistException {
		assert (!name.equals("")) : "Invalid parameter \"name\"";
		assert (server != null) : "Invalid parameter \"server\"";

		if (!dataServer.containsKey(name)) {
			dataServer.put(name, server);
		} else {
			throw new ServerAlreadyExistException();
		}
	}

	/**
	 * This method delate the DataServer from the Dictionary, if it is present.
	 * 
	 * @param name
	 * @throws UnavailableServerException
	 */
	public static void logOUT(String name) throws UnavailableServerException {
		assert (!name.equals("")) : "Invalid parameter \"name\"";

		if (dataServer.containsKey(name)) {
			dataServer.remove(name);
		} else {
			throw new UnavailableServerException();
		}
	}

}
