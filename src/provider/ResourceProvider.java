package provider;

import java.util.Iterator;

import fif_core.ResourceRegister;
import provider.exceptions.ConnectionFailException;
import provider.exceptions.ConnectionFaitException;
import provider.exceptions.UnavailableServerException;
import provider.resourceBuilder.FifResource;

/**
 * Represent the implementation of class Provider.
 * 
 * @author Iaffaldano Giuseppe M.587556
 * @author Cavallo Giacomo M.588009
 * 
 * @param <T>
 */
public class ResourceProvider<T> implements Provider<T> {
	
	/**
	 * Represent the linked DataServer
	 */
	private DataServer myServer;

	/**
	 * Represent the register if we use it.
	 */
	private ResourceRegister register = null;

	/**
	 * First constructor of class, it create a Resource Provider with register.
	 * 
	 * @param about
	 *            - Represent the configuration String.
	 * @param register
	 *            - Represent the register resources.
	 * @throws UnavailableServerException
	 * @throws ConnectionFaitException
	 */
	public ResourceProvider(String about, ResourceRegister register)
			throws UnavailableServerException, ConnectionFailException {
		assert (!about.equals("")) : "Invalid parameter \"about\"";

		Factory myFactory = new ServerFactory();
		this.register = register;
		//restituisce un server configurato e connesso al db con le transazioni lette
		this.myServer = myFactory.getServer(about);
	}

	/**
	 * Second constructor of class, it create a Resource Provider without
	 * register.
	 * 
	 * @param about
	 *            - Represent the configuration String.
	 * @throws UnavailableServerException
	 * @throws ConnectionFaitException
	 */
	public ResourceProvider(String about) throws UnavailableServerException, ConnectionFailException {
		assert (!about.equals("")) : "Invalid parameter \"about\"";

		Factory myFactory = new ServerFactory();
		this.myServer = myFactory.getServer(about);
	}

	/**
	 * Represent the implementation of method iterator of class Iterable.
	 * 
	 * @return the ResourceProviderRegisterIterator if register is used
	 *         otherwise return ResourceProviderResourceIterator.
	 */
	@Override
	public Iterator iterator() {
		if (this.register != null) {
			return new ResourceProviderRegisterIterator();
		} else {
			return new ResourceProviderResourceIterator();
		}
	}

	/**
	 * Represent the innerclass to implement the iterator with register.
	 * 
	 * @author giacomocavallo
	 * 
	 * @param <ResourceRegister>
	 *            - Class to represent the resources register in fif-core.
	 */
	private class ResourceProviderRegisterIterator<ResourceRegister> implements Iterator<ResourceRegister> {

		/**
		 * @return true if there are other element in the DataServer
		 */
		@Override
		public boolean hasNext() {
			return myServer.hasNext();
		}

		/**
		 * Add the next element of the DataServer in the register.
		 * 
		 * @return The register with the new element.
		 */
		@Override
		public ResourceRegister next() {
			FifResource risorsa = (FifResource) myServer.next();
			System.out.println(risorsa);
			register.associateDescriptor(((provider.resourceBuilder.FifResource) risorsa).getResourceName(),
					((provider.resourceBuilder.FifResource) risorsa).getDescriptor());
			return (ResourceRegister) register;
		}

		/**
		 * Don't use this method because is Unsupported.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();

		}

	}

	/**
	 * Represent the innerclass to implement the iterator without register.
	 * 
	 * @author giacomocavallo
	 * 
	 * @param <FifResource>
	 *            - Class to represent the resources
	 */
	private class ResourceProviderResourceIterator<FifResource> implements Iterator<FifResource> {

		/**
		 * @return true if there are other element in the DataServer.
		 */
		@Override
		public boolean hasNext() {
			return myServer.hasNext();
		}

		/**
		 * @return the next element in the DataServer.
		 */
		@Override
		public FifResource next() {
			FifResource risorsa = (FifResource) myServer.next();
			System.out.println("\nResourceProvider: Next Resource is:\n"+risorsa+"\n");
			return risorsa;
		}

		/**
		 * Don't use this method because is Unsupported.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();

		}

	}

}
