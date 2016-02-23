package fif_possibilisticLearning.proximityTable;

import java.io.IOException;
import java.util.HashMap;
import fif_learning.ProximityTable;
import openness.utility.ProximityTableWriter;

public abstract class ProximityTableAttribute {

	// per ogni attributo su cui calcolare fuzzy set,una proximity table
	private static HashMap<String, ProximityTable> pta = ProximityTableWriter.getProxTables();
	
	protected ProximityTableAttribute() {
	}

	public static void setProximityTables(HashMap<String, ProximityTable> proxTables) {
		pta = proxTables;
	}

	public static HashMap<String, ProximityTable> getPts() {
		return pta;
	}

	

	abstract protected ProximityTable generate() throws IOException;

	
}
