package fif_learning.principles;

import java.util.HashSet;
import java.util.Set;

import fif_learning.*;
import fif_core.FuzzySet;
import fif_core.Metadata;

public class Temporal extends Locality {
	/*
	 * nota bene. temporal opera su dati memorizzati già presenti in un filtro.
	 * In poche parole può essere applicato in conocmitanza con altri
	 * principi,altrimenti resitituirà solo 0
	 */
	@Override
	public void apply(Metadata mResource, Metadata mFilter) {

		double temporalRelevance = Double.parseDouble(FilterUpdate.userConfigMap.get("temporalthreshold"));

		assert (temporalRelevance <= 1 && temporalRelevance >= 0) : "Error in configuration file";
		System.out.println("\nApplico TEMPORAL " + temporalRelevance);
		FuzzySet fsResource = mResource.getFuzzySet();
		Set<String> newSet = fsResource.getSupport();
		double d = 0;
		System.out.println("\nprima di temporal mj=" + FilterUpdate.memorizedData);
		System.out.println("\nprima di temporal rs=" + fsResource.getHash());
		if (!FilterUpdate.memorizedData.isEmpty()) {
			/*
			 * Moltiplica i dati memorizzati per la rilevanza temporale
			 * importanza
			 * 
			 */
			Set<String> sss = new HashSet<String>(FilterUpdate.memorizedData.keySet());
			for (String ss : sss) {
				d = FilterUpdate.memorizedData.get(ss) * temporalRelevance;
				FilterUpdate.memorizedData.remove(ss);
				FilterUpdate.memorizedData.put(ss, d);
				System.err.println(ss + "  " + d);
			}
		}
		System.out.println("dopo la moltiplicazione mj=" + FilterUpdate.memorizedData);
		for (String s : newSet) {
			// System.out.println("aj="+s+"
			// FuzzyValue="+fsResource.getValue(s));
			if (FilterUpdate.memorizedData.containsKey(s)) {
				/*
				 * perche' qui di nuovo moltiplicare per la rilevanza temporale?
				 */
				Double precValue = (temporalRelevance * FilterUpdate.memorizedData.get(s));
				if (Math.max(fsResource.getValue(s), precValue) == precValue) {
					fsResource.removeElement(s);
					fsResource.setValue(s, precValue);
					FilterUpdate.memorizedData.remove(s);
					FilterUpdate.memorizedData.put(s, precValue);
				} else {
					Double currentValue = fsResource.getValue(s);
					FilterUpdate.memorizedData.remove(s);
					FilterUpdate.memorizedData.put(s, currentValue);
				}
			} else {
				FilterUpdate.memorizedData.put(s, fsResource.getValue(s));
			}
		}
		for (String s : FilterUpdate.memorizedData.keySet()) {
			fsResource.removeElement(s);
			fsResource.setValue(s, FilterUpdate.memorizedData.get(s));
		}
		System.out.println("mj=" + FilterUpdate.memorizedData);
	}
}
