package provider.resourceBuilder;

import fif_core.*;

/**
 * This class represent the structure useful to build new FifResource. It must
 * be extended from each DataServer.
 * 
 * @author Iaffaldano Giuseppe M.587556
 * @author Cavallo Giacomo M.588009
 *
 */
public abstract class FifResourceBuilder {

	/**
	 * Useful to create new FifResource.
	 * 
	 * @return new FifResource.
	 */
	public abstract FifResource build();

	/**
	 * Sets the name of the resource
	 * 
	 * @param resource
	 *            - Is the String name of the resource.
	 */
	public abstract void setResource(String resource);

	/**
	 * Add new Metadata to the resource.
	 * 
	 * @param fs
	 *            - Is the FuzzySet content the Data of MetaData.
	 * @param att
	 *            - Is the corrispondent attribute of the FuzzySet.
	 */
	public abstract void addMetadata(FuzzySet fs, Attribute att);
}
