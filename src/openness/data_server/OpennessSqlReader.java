package openness.data_server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import provider.sql_connection.SqlReader;
import provider.sql_connection.exception.DriverNotSupportedException;
import provider.sql_connection.exception.SqlServerNotRechableException;

/**
 * Allow to query the Sql Server
 * 
 * @author Alessandro Antonacci 590320
 *
 */
public class OpennessSqlReader {
	private SqlReader reader;
	private String driver;
	private String connectionUrl;
	private String attributes;
	private String table;
	private String id;
	private String fuzzy;
	private String userId;

	public final static String DATE = "-date";

	// Query parts
	private String SELECT_QUERY = "SELECT ";
	private String FROM_QUERY = " FROM ";
	private String WHERE_QUERY = " WHERE ";

	/**
	 * Constructor that allows to configure the SqlReader
	 * 
	 * @param conf
	 *            string containing the configuration parameters for the
	 *            SqlReader
	 */
	public OpennessSqlReader(String conf) {
		assert (conf != null) : "Null parameter";
		this.setParameters(conf);

		try {
			reader = new SqlReader(driver, connectionUrl);
		} catch (DriverNotSupportedException | SqlServerNotRechableException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	// setta i parametri del reader in base a ciò che legge dal file xml
	private void setParameters(String conf) {
		assert (conf.length() != 0) : "Empty configuration";

		String[] confArray = conf.split(" ");
		System.out.println(
				"OpennessSqlReader--------------------------------------\n configuration parameters read from xml:");
		for (String s : confArray)
			System.out.println(s);
		for (int i = 0; i < confArray.length; i++) {
			String element = confArray[i];

			if (element.equalsIgnoreCase(OpennessSqlDataServer.DRIVER)) {
				if (!confArray[i + 1].contains("-"))
					driver = confArray[++i];
			}
			if (element.equalsIgnoreCase(OpennessSqlDataServer.CONNECTION_URL)) {
				if (!confArray[i + 1].contains("-"))
					connectionUrl = confArray[++i];
			}

			if (element.equalsIgnoreCase(OpennessSqlDataServer.ATTRIBUTES)) {
				if (!confArray[i + 1].contains("-")) {
					attributes = confArray[++i];
					System.out.println("Gli attributi sono: " + attributes + "\n");
				}
			}
			if (element.equalsIgnoreCase(OpennessSqlDataServer.TABLE)) {
				if (!confArray[i + 1].contains("-")) {
					table = confArray[++i];
					System.out.println("La tabella é: " + table + "\n");
				}
			}
			if (element.equalsIgnoreCase(OpennessSqlDataServer.ID)) {
				if (!confArray[i + 1].contains("-")) {
					id = confArray[++i];
					System.out.println("La ID risorsa é: " + id + "\n");
				}
			}
			if (element.equalsIgnoreCase(OpennessSqlDataServer.FUZZY)) {
				if (!confArray[i + 1].contains("-")) {
					fuzzy = confArray[++i];
					System.out.println("Gli aggettivi su cui calcolare i fuzzy set sono: " + fuzzy + "\n");
				}
			}
			if (element.equalsIgnoreCase(OpennessSqlDataServer.USER_ID)) {
				if (!confArray[i + 1].contains("-")) {
					userId = confArray[++i];
					System.out.println("L'id dell'utente su cui calcolare i fuzzy set è: " + userId + "\n");
				}
			}
		}
		SELECT_QUERY += attributes;
		FROM_QUERY += table;
		WHERE_QUERY += " userId= " + userId + " ;";
	}

	/**
	 * Allows to retrieve the users rows from the sql Database, transforming
	 * them in a list of Transactions.
	 * 
	 * @return ArrayList containing the transactions.
	 */
	public ArrayList<Transaction> getUserRows() {
		String query = SELECT_QUERY + FROM_QUERY + WHERE_QUERY;// +

		ArrayList<HashMap<String, String>> result;
		try {
			result = reader.execute(query);

			return this.getTransactions(result, id, fuzzy);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	// restituisce un'arrayList di arraylist di stringhe che contiene
	// tutti i valori degli attributi su cui calcolare il fuzzyset
	// nel mio caso ad esempio tutti i valori di type e classname (ma possono
	// anche essserci più attributi),utilizzato in proximity table writer per
	// scrivere la tabella senza sapere a priori gli attributi e i valori che
	// assumeranno
	public HashMap<String, ArrayList<String>> getFuzzyAttributeValues() {

		// array di stringhe contenenti i vari attributi su cui calcolare
		// fuzzy,arrivati dalla string
		String[] fuzzyAttributes = fuzzy.split(",");
		// hashmap contenente attributo-valori attributo
		HashMap<String, ArrayList<String>> fuzzyAttributeValues = new HashMap<String, ArrayList<String>>();

		for (int i = 0; i < fuzzyAttributes.length; i++) {
			SqlReader r;
			try {
				r = new SqlReader(driver, connectionUrl);

				// preleva i valori di ogni attributo su cui calcolare fuzzy
				ArrayList<HashMap<String, String>> res = r
						.execute("SELECT DISTINCT " + fuzzyAttributes[i] + " FROM " + table + " ;");
				ArrayList<String> values = new ArrayList<>();
				for (HashMap<String, String> result : res) {
					values.addAll((result.values()));
				}
				fuzzyAttributeValues.put(fuzzyAttributes[i], values);
			} catch (DriverNotSupportedException | SqlServerNotRechableException | SQLException e) {
				e.printStackTrace();
			}
		}
		return fuzzyAttributeValues;
	}

	// viene utiilizzata per ricevere degli oggetti di tipo transaction dati i
	// dati prelevati dal db
	private ArrayList<Transaction> getTransactions(ArrayList<HashMap<String, String>> al, String id, String fuzzy) {
		assert (al != null) : "Null parameter";
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		for (HashMap<String, String> h : al) {
			transactions.add(new Transaction(h, id, fuzzy));
		}
		return transactions;
	}
}
