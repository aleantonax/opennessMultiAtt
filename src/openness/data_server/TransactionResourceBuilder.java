package openness.data_server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import fif_core.*;
import fif_core.exceptions.MetadataWithSameAttributeException;
import provider.resourceBuilder.FifResource;
import provider.resourceBuilder.FifResourceBuilder;

/**
 * Allows to create a FifResource according to Fif-Core format.
 * 
 * @autorho Alessandro Antonacci 590320
 * @author Francesco Centrone 608758
 * 
 */
// costruisce risorse fif a partire dal db.
public class TransactionResourceBuilder extends FifResourceBuilder {
	// antonacci: il fuzzy set può essere calcolato su più attributi ora,non
	// solo su type
	public final static ArrayList<Attribute> FUZZY_ATT = new ArrayList<Attribute>();

	private ArrayList<Metadata> metadata = new ArrayList<Metadata>();
	private Resource resource;
	/**
	 * Builds a Resource representing a Transaction according to Fif-Core.
	 * 
	 * @param t
	 *            - transaction to represent
	 * 
	 */
	// l'attributo è rappresentato da una sola stringa. se sono presenti più
	// attributi,essi saranno separati da una virgola e opportunamente
	// considerati nella creazione della risorsa fif
	public TransactionResourceBuilder(Transaction t, String attributeForId, String attributesForFuzzy) {
		assert (t != null) : "Null parameter";
		setResource(t.get(attributeForId)); 
		// se gli attributi su cui calcolare il fuzzy sono di più (viene letta
		// una stringa con diversi attributi separati dalla virgola,generata in
		// xmlreader)
		if (attributesForFuzzy.contains(",")) {
			String[] strArr = attributesForFuzzy.split(",");
			for (int i = 0; i < strArr.length; i++) {
				FuzzySet a = new FuzzySet();
				// imposta per ogni valore dell'attributo il fuzzy set
				// (valoreAttributo,1)
				a.setValue(t.get(strArr[i]), 1); 
				// per ogni attributo passato per parametro viene aggiunto un
				// metadato alla risorsa
				addMetadata(a, new Attribute(strArr[i]));
				System.out.println("*********************************** Transaction resource builder: metadata: "
						+ a.getHash().toString());
			}
		} else {
			FuzzySet fs_transaction = new FuzzySet();

			// imposta il valore corrispondente all'attributo(colonna del db) su
			// cui calcolare
			// il fuzzyset
			fs_transaction.setValue(t.get(attributesForFuzzy), 1);
			addMetadata(fs_transaction, new Attribute(attributesForFuzzy));
		}
	}

	/**
	 * Builds the complete resource from available data.
	 * 
	 * @return FifResource
	 */
	@Override
	public FifResource build() {
		assert (this.resource != null) : "Empty data \"resource\"";
		assert (this.metadata != null) : "Empty data \"metadata\"";

		Descriptor transactionDesc = new Descriptor();
		try {
			transactionDesc.setMetadata(this.metadata);

		} catch (MetadataWithSameAttributeException e) {
			e.printStackTrace();
		}
		return new FifResource(this.resource, transactionDesc);
	}

	/**
	 * Associates a new Resource to the resource to build.
	 * 
	 * @param resources
	 *            - String representing URI
	 */
	@Override
	public void setResource(String transactionID) {
		assert (!resource.equals("")) : "Invalid parameter \"resource\"";
		String iri = transactionID;
		URI resourceURI;

		try {
			resourceURI = new URI(iri);
			this.resource = new Resource(resourceURI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Associates a new Metadata to the resource to build.
	 * 
	 * @param fs
	 *            - FuzzySet representing data
	 * @param att
	 *            - Attribute name
	 */
	@Override
	public void addMetadata(FuzzySet fs, Attribute att) {
		assert (fs != null) : "Empty parameter \"FuzzySet\"";
		assert (att != null) : "Empty parameter \"Attribute\"";

		this.metadata.add(new Metadata(att, fs, PossibilisticInterpretation.getinstance()));
	}
}
