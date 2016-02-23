package fif_learning.principles;

import java.util.Set;

import fif_core.FuzzySet;
import fif_core.Metadata;
import fif_learning.FilterUpdate;

/**
 * Applies the observation relevance principle on a resource's fuzzy set.
 * 
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 */
public class Observation implements Principle {
	/*
	 * nota bene. modificando il valore della risorsa,non verrà mai soddisfatta
	 * la condizione dell'if,in quanto la risorsa(osservazione) viene modificata
	 * se e solo se il valore osservato è MINORE di 1-threshold,ma nel nostro
	 * caso ogni osservazione ha valore 1 quindi non avrà mai valore minore di
	 * 1-threshold
	 */
	public void apply(Metadata mResource, Metadata mFilters) {

		double obsThreshold = Double.parseDouble(FilterUpdate.userConfigMap.get("observationthreshold"));

		System.out.println("\nApplico OBSERVATION " + obsThreshold);
		FuzzySet fsResource = mResource.getFuzzySet();
		Set<String> fsString = fsResource.getSupport();

		// centrone: TODO QUI C'e' UN BUG!!!!!
		for (String s : fsString) {
			if (fsResource.getValue(s) < 1 - obsThreshold) {
				fsResource.removeElement(s);
				fsResource.setValue(s, 1 - obsThreshold);
			}

		}
	}
}
