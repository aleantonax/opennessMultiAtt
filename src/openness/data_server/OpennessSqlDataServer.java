package openness.data_server;

import java.util.ArrayList;
import openness.utility.ProximityTableWriter;
import provider.DataServer;
import provider.exceptions.ConnectionFailException;
import provider.exceptions.ConnectionFaitException;
import provider.resourceBuilder.FifResource;

/**
 * Extract all resources from the Openness SQL server
 * 
 * @author Alessandro Antonacci
 *
 */
public class OpennessSqlDataServer extends DataServer {
	/**
	 * Tag string for the configuration of the parameter DRIVER
	 */
	public final static String DRIVER = "-driver";
	/**
	 * Tag string for the configuration of the parameter CONNECTION URL
	 */
	public final static String CONNECTION_URL = "-connectionUrl";
	// aggiunta antonacci
	public final static String ATTRIBUTES = "-attributes";

	public final static String TABLE = "-table";

	public final static String ID = "-id";

	public final static String FUZZY = "-fuzzy";
	
	public final static String USER_ID = "-userId";


	private int current = 0;
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	

	/**
	 * Returns true if exists another resource, returns false otherwise.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean hasNext() {
		return transactions.size() > current;
	}

	/**
	 * Returns the next resource available.
	 * 
	 * Returns null if no info for the resource is found.
	 * 
	 * @return FifResource
	 */
	@Override
	public FifResource next() {
		TransactionResourceBuilder newResourceBuilder = new TransactionResourceBuilder(transactions.get(current),
				transactions.get(current).getId(), transactions.get(current).getFuzzy());
		FifResource temp = newResourceBuilder.build();
		current++;
		return temp;

	}

	/**
	 * Configures local server with specified options.
	 * 
	 * @param info
	 *            - options for the configuration
	 * @throws ConnectionFaitException
	 */
	@Override
	public void configureServer(String info) throws ConnectionFailException {
		assert (info != null) : "Null parameter";
		OpennessSqlReader sr = new OpennessSqlReader(info);
		transactions = sr.getUserRows();
		
		//imposta gli attributi su cui calcolare la proximity table,poi la scrive
		ProximityTableWriter.setFuzzyAttributes(sr.getFuzzyAttributeValues());
		ProximityTableWriter.writeProximityTable();
		current=0;

	}

}
