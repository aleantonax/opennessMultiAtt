package fif_learning.principles;

import java.util.Set;

import fif_core.FuzzySet;
import fif_core.Metadata;
import fif_learning.FilterUpdate;

/**
 * Provides functionalities strictly related to the learning phase in a filter's
 * life-cycle, with the minimum possibility relevance criteria as the base
 * criteria for the update of a filter.
 * 
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public class BaseRelevanceCriteria {

	/**
	 * Applies the minimum possibility relevance principle between two Metadata.
	 * 
	 * @param mResource
	 * @param mFilter
	 * @return
	 */
	public static FuzzySet minPosRelevance(Metadata mResource, Metadata mFilter) {

		
		String observation = FilterUpdate.userConfigMap.get("observation");
		String knowledge = FilterUpdate.userConfigMap.get("knowledge");

		FuzzySet resourceMeta = mResource.getFuzzySet();
		Set<String> resourceSet = resourceMeta.getSupport();
		FuzzySet filterMeta = mFilter.getFuzzySet();

		System.out.println("\nAPPLICO MIN_POS_RELEVANCE");
		System.out.println("FILTER META : " + filterMeta.getHash());
		System.out.println("RESOURCE META : " + resourceMeta.getHash());

		Set<String> filterSet = filterMeta.getSupport();
		double i = 0;
		// applica il principio di minima specificità
		for (String s1 : resourceSet) {
			if (filterMeta.getHash().containsKey(s1)) {
				i = filterMeta.getValue(s1);
				if (resourceMeta.getValue(s1) <= i) {
					filterMeta.removeElement(s1);
					filterMeta.setValue(s1, resourceMeta.getValue(s1));
				}
			} else {
				if (knowledge.equalsIgnoreCase("true"))
					filterMeta.setValue(s1, resourceMeta.getValue(s1));
				else
					filterMeta.setValue(s1, 0);
			}
		}
		if (observation.equalsIgnoreCase("true") || knowledge.equalsIgnoreCase("true")) {
			for (String s2 : filterSet) {
				if (!resourceMeta.getHash().containsKey(s2)) {
					filterMeta.removeElement(s2);
					filterMeta.setValue(s2, 0);
				}
			}
		}

		return filterMeta;
	}

}
