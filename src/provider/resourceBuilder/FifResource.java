package provider.resourceBuilder;

import java.util.AbstractList;
import java.util.Set;

import fif_core.Attribute;
import fif_core.Descriptor;
import fif_core.FuzzySet;
import fif_core.Metadata;
import fif_core.Resource;

/**
 * The class represent the Resource.
 * 
 * @author Iaffaldano Giuseppe M.587556
 * @author Cavallo Giacomo M.588009
 *
 */
public class FifResource {

	/**
	 * Represents the name of the resource in IRI.
	 */
	private Resource resource;
	/**
	 * Represents the description of the resource.
	 */
	private Descriptor resourceDescription;

	/**
	 * Simple constructor.
	 * 
	 * @param r
	 *            - Resource.
	 * @param rd
	 *            - Resource descriptor.
	 */
	public FifResource(Resource r, Descriptor rd) {
		assert (r != null) : "Invalid parameter \"r\"";
		assert (rd != null) : "Invalid parameter \"rd\"";

		this.resource = r;
		this.resourceDescription = rd;

	}

	/**
	 * Return the name of the resource
	 * 
	 * @return Resource name
	 */
	public Resource getResourceName() {
		return this.resource;
	}

	/**
	 * Return the description of the resource
	 * 
	 * @return Resource description
	 */
	public Descriptor getDescriptor() {
		return this.resourceDescription;
	}

	/**
	 * Simple toString of the resource, it include all of Metadata.
	 * 
	 * @return String that represent the FifResource
	 */
	public String toString() {
		String result = "FifResource:    Risorsa: ";
		result += "  " + this.resource.getUri().toASCIIString() + "\n";
		result += "    Descrizione... \n";
		AbstractList<Metadata> meta = this.resourceDescription.getAllMetadata();
		for (Metadata m : meta) {
			Attribute a = m.getAttribute();
			result += "    Attributo: " + a.getAttributeName() + "\n";
			FuzzySet f = m.getFuzzySet();
			Set<String> value = f.getSupport();
			for (String v : value) {
				result += "        " + v + " : " + f.getValue(v) + "\n";
			}
		}

		return result;
	}
}
