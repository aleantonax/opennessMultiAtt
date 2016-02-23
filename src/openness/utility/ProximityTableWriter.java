package openness.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fif_learning.ProximityTable;
import fif_possibilisticLearning.proximityTable.ProximityTableAttribute;

/**
 * classe di utility che permette di scrivere delle proximity table per ogni
 * attributo passato come parametro
 * 
 * @author Alessandro Antonacci
 * 
 */
public class ProximityTableWriter {
	// Antonacci
	private static HashMap<String, ArrayList<String>> fuzzyAttributes;
	private static HashMap<String, ProximityTable> proxTables = new HashMap<>();

	/**
	 * imposta per ciascun utente,quali attributi utilizzare per scrivere la
	 * proximity table
	 * 
	 * @param att
	 *            una mappa contenente per ciascun utente gli attributi per cui
	 *            scrivere la proximity table
	 */
	public static void setFuzzyAttributes(HashMap<String, ArrayList<String>> att) {
		fuzzyAttributes = att;
	}

	/**
	 * restituisce le proximity table
	 * 
	 * @return una mappa contenente,per ciascun attributo,la sua proximity table
	 * 
	 */

	public static HashMap<String, ProximityTable> getProxTables() {
		return proxTables;
	}

	// verifica se la prima parola tra due stringhe è uguale e conta le parole
	// in comune
	private static double getSimilarity(String a, String b) {
		// regexp per splittare la stringa a seconda che contenga .,_ o entrambi
		// (utile per gli attributi presenti nel db)
		String[] splitA = a.split("[\\._]");
		String[] splitB = b.split("[\\._]");
		boolean firstWordInCommon = false;
		double wordsInCommon = 0;
		if (splitA.length > splitB.length) {
			for (int i = 0; i < splitA.length; i++) {
				for (int j = 0; j < splitB.length; j++) {

					if (splitA[i].equalsIgnoreCase(splitB[j])) {
						if (i == 0 && j == 0)
							firstWordInCommon = true;
						wordsInCommon++;

					}
				}
			}
		} else {
			for (int j = 0; j < splitB.length; j++) {
				for (int i = 0; i < splitA.length; i++) {
					if (splitA[i].equalsIgnoreCase(splitB[j])) {
						if (i == 0 && j == 0)
							firstWordInCommon = true;
						wordsInCommon++;
					}
				}
			}
		}
		double toReturn = 0;
		if (firstWordInCommon) {
			toReturn = 0.5;
		}
		toReturn += wordsInCommon / ((splitB.length + splitA.length) / 2) * 0.5;
		return toReturn;
	}

	// scrive per ogni attributo fuzzy scritto nel file xml una tabella di
	// prossimità
	public static void writeProximityTable() {
		int m = 0;
		// crea una tabella di prossimità per ogni attributo
		for (ArrayList<String> fuzzyAtt : fuzzyAttributes.values()) {
			ProximityTable table = new ProximityTable();
			try {
				// nome del file
				FileWriter writer = new FileWriter("openness_prox_tables\\"+String.valueOf(fuzzyAtt.hashCode() + ".txt"));

				for (int i = 0; i < fuzzyAtt.size() - 1; i++) {
					for (int j = i + 1; j < fuzzyAtt.size(); j++) {
						writer.append("table.insert(\"" + fuzzyAtt.get(i) + "\",\"" + fuzzyAtt.get(j) + "\","
								+ getSimilarity(fuzzyAtt.get(i), fuzzyAtt.get(j)) + ");");
						writer.append("\n");
						table.insert(fuzzyAtt.get(i), fuzzyAtt.get(j), getSimilarity(fuzzyAtt.get(i), fuzzyAtt.get(j)));
						// feedback
						// System.out.println("table.insert(\"" +
						// fuzzyAtt.get(i) + "\",\"" + fuzzyAtt.get(j) + "\","
						// + getSimilarity(fuzzyAtt.get(i), fuzzyAtt.get(j)) +
						// ");");

					}
				}
				// inserisco la tabella nell'hashmap attributo-tabella di
				// prossimità di quell'attributo
				proxTables.put((String) fuzzyAttributes.keySet().toArray()[m], table);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			m++;
		}
		ProximityTableAttribute.setProximityTables(proxTables);
	}
}