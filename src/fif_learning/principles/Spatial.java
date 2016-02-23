package fif_learning.principles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import fif_core.FuzzySet;
import fif_core.Metadata;
import fif_learning.FilterUpdate;
import fif_learning.ProximityTable;
import fif_possibilisticLearning.proximityTable.ProximityTableAttribute;

public class Spatial extends Locality {

	ProximityTable table;
	HashMap<String, ProximityTable> tables;

	@Override
	public void apply(Metadata mResource, Metadata mFilter) throws IOException {
		System.out.println("\nApplico SPATIAL");

		String spatialFlag = FilterUpdate.userConfigMap.get("spatial");

		if (spatialFlag.equalsIgnoreCase("true")) {

			FuzzySet fsResource = mResource.getFuzzySet();
			Set<String> setResource = fsResource.getSupport();
			/*
			 * Aggiunta Centrone 608758
			 */
			FilterUpdate.aj.clear();
			/*
			 * Fine Aggiunta
			 */
			/*
			 * Grafo similarità: A seconda dell'attributo impostato nel file
			 * config restituisce il grafo relativo
			 */
			System.out.println("Caricamento grafo");
			tables = ProximityTableAttribute.getPts();
			System.out.println("Grafo caricato");

			/*
			 * Lista distinct dei valori del dominio di un attributo Aj: A
			 * seconda dell'attributo impostato nel file config restituisce la
			 * lista relativa
			 */
			for (String fuzzyAtt : tables.keySet()) {
				ProximityTable temp;
				FuzzySet fs;
				HashSet<String> listMember = new HashSet<String>();
				ArrayList<String> list = new ArrayList<String>();
				
				/*
				 * Aggiunto da centrone 608758
				 */
				listMember = tables.get(fuzzyAtt).extractAllMembers();

				/*
				 * Filtraggio: Tutti gli elementi del dominio - elementi nella
				 * corrente osservazione
				 * 
				 */
				for (String member : listMember) {
					if (!fsResource.contains(member)) {
						list.add(member);
					}
				}

				/*
				 * Per ogni elemento del filtraggio si calcola il valore di
				 * similarità con gli elementi nella corrente osservazione. Tale
				 * elemento viene inserito in a'j
				 * 
				 */

				for (String m : list) {
					// Aj - aj = tutto il dominio tranne l'osservazione corrente
					fs = tables.get(fuzzyAtt).getFuzzySet(m); // tutti i simili
																// di u
					temp = new ProximityTable();
					for (String sResource : setResource) {
						if (fs.contains(sResource)) { // se è presente nei
														// simili di
														// u
							temp.insert(m, sResource, fs.getHash().get(sResource) * fsResource.getValue(sResource)); // nel
																														// caso
																														// lo
																														// aggiunge
						}
					}
					FilterUpdate.aj.put(m, temp.getMaxValue());
				}

				for (String sResource : setResource) {
					FilterUpdate.aj.put(sResource, fsResource.getValue(sResource));
				}
				/*
				 * Aggiunta Centrone 608758 Aggiorno risorsa
				 */
				Set<String> ajStrings = FilterUpdate.aj.keySet();
				for (String s : ajStrings) {
					mResource.getFuzzySet().setValue(s, FilterUpdate.aj.get(s));
					// System.out.println("add to resource "+s+" = "+
					// FilterUpdate.aj.get(s));
				}
			}
		}
	}
}
