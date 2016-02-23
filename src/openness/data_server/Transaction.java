package openness.data_server;

import java.util.HashMap;

/**
 * It Represent the Transfer Object for the communication from the database and
 * the other levels of the project
 * 
 * @author Alessandro Antonacci 590320
 *
 */
public class Transaction {

	private String id;
	private String fuzzy;
	private HashMap<String, String> values = new HashMap<String, String>();

	/**
	 * Constructor that builds the object from an HashMap
	 * 
	 * @param h
	 *            HashMap conteining the parameters
	 * @param fuzzy
	 * @param id
	 */
	public Transaction(HashMap<String, String> h, String id, String fuzzy) {
		super();
		assert (h != null) : "hashMap is null";
		values = h;
		this.id = id;
		this.fuzzy = fuzzy;

	}

	// restituisce valore di un attributo
	public String get(String attribute) {
		return values.get(attribute);
	}

	public String getId() {
		return this.id;
	}

	public String getFuzzy() {
		return fuzzy;
	}

}
