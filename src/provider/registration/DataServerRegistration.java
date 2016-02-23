package provider.registration;

import provider.exceptions.ServerAlreadyExistException;
import provider.exceptions.UnavailableServerException;

/**
 * This class represent the structure of DataServer Registration, and each
 * linked DataServer must extend it.
 * 
 * @author Iaffaldano Giuseppe M.587556
 * @author Cavallo Giacomo M.588009
 * 
 */
public abstract class DataServerRegistration {

	/**
	 * Allows the registration of DataServer to the Fif-Provider.
	 * 
	 * @throws ServerAlreadyExistException
	 */
	public abstract void logIN() throws ServerAlreadyExistException;

	/**
	 * Allows the logOut of the DataServer to the Fif-Provider.
	 * 
	 * @throws UnavailableServerException
	 */
	public abstract void logOUT() throws UnavailableServerException;

}
