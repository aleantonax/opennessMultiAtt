package fif_core;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Hashtable;

import fif_core.exceptions.MetadataWithSameAttributeException;

/**
 * This class provides implementation and methods of an descriptor.<br>
 * <br>
 * One descriptor has a set of metadata of one resource and can't have more than
 * one metadata with the same attribute.
 * 
 * @author Troiano Lorenzo
 * @version 1.0
 */

public class Descriptor {

	private AbstractList<Metadata> metadata;
	private Hashtable<String, Metadata> associations = new Hashtable<String, Metadata>();

	/**
	 * Sets a list of metadata for the current descriptor.<br>
	 * <br>
	 * 
	 * You can set a variable numbers of metadata.<br>
	 * <br>
	 * 
	 * Preconditions: <br>
	 * <br>
	 * 1-None of the metadata must be null.<br>
	 * 2-Can't be set two or more metadata with the same attribute.<br>
	 * <br>
	 * 
	 * Postconditions:<br>
	 * <br>
	 * 1-All the metadata are set in the descriptor correctly.<br>
	 * <br>
	 * 
	 * @param meta
	 *            The list of metadata value you want to set.
	 * @throws MetadataWithSameAttributeException
	 *             if at least two metadata have the same attribute.
	 */

	public void setMetadata(Metadata... meta) throws MetadataWithSameAttributeException {
		assert (areMetadataNotNull(meta)) : "Descriptor: at least one of the metadata is null";

		this.metadata = new CheckedLinkedList<Metadata>();
		for (int i = 0; i < meta.length; i++) {
			if (!this.associations.containsKey(meta[i].getAttribute().getAttributeName())) {
				this.associations.put(meta[i].getAttribute().getAttributeName(), meta[i]);
				this.metadata.add(meta[i]);
			} else {
				throw new MetadataWithSameAttributeException();
			}
		}
	}

	/**
	 * Returns an abstract list containing all the metadata of this descriptor.
	 * <br>
	 * <br>
	 * 
	 * You can modify internal state of the descriptor calling this method and
	 * then calling add/remove etc. methods of the abstract list.<br>
	 * <br>
	 * 
	 * Postconditions:<br>
	 * <br>
	 * 1-It's returned an abstract list containing all the metadata of this
	 * descriptor.<br>
	 * <br>
	 * 
	 * @return an abstract list containing all the metadata of this descriptor.
	 */

	public AbstractList<Metadata> getAllMetadata() {

		return metadata;
	}

	/**
	 * Returns a metadata that has the specific attribute.<br>
	 * <br>
	 * 
	 * 
	 * Postconditions:<br>
	 * <br>
	 * 1-It's returned the metadata if it's found a metadata with that attribute
	 * or null if there are not metadata with that attribute.<br>
	 * 
	 * @return the metadata that has the specific attribute.
	 */

	public Metadata getMetadata(String s) {

		return associations.get(s);
	}

	private boolean areMetadataNotNull(Metadata[] meta) {

		boolean isValid = true;

		for (int i = 0; i < meta.length; i++) {

			isValid = isValid && meta[i] != null;
		}

		return isValid;

	}

	public void setMetadata(ArrayList<Metadata> meta) throws MetadataWithSameAttributeException {
		assert (areMetadataNotNull((Metadata[]) meta.toArray())) : "Descriptor: at least one of the metadata is null";

		this.metadata = new CheckedLinkedList<Metadata>();
		for (int i = 0; i < meta.size(); i++) {
			if (!this.associations.containsKey(((Metadata) meta.get(i)).getAttribute().getAttributeName())) {
				this.associations.put(((Metadata) meta.get(i)).getAttribute().getAttributeName(),
						(Metadata) meta.get(i));
				this.metadata.add((Metadata) meta.get(i));
			} else {
				throw new MetadataWithSameAttributeException();
			}
		}
	}

}
