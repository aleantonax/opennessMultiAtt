package fif_learning.principles;

import java.util.Set;

import fif_core.FuzzySet;
import fif_core.Metadata;
import fif_learning.FilterUpdate;

public class Knowledge extends Relevance {

	/**
	 * Applies the knowledge relevance principle on a filter's fuzzy set.
	 */

	public void apply(Metadata mResource, Metadata mFilter) {

		String knowledgeFlag = FilterUpdate.userConfigMap.get("knowledge");
		double knowledgeThreshold = Double.parseDouble(FilterUpdate.userConfigMap.get("knowledgethreshold"));
		System.out.println("\nApplico KNOWLEDGE " + knowledgeThreshold);

		if (knowledgeFlag.equalsIgnoreCase("true")) {
			FuzzySet fsFilter = mFilter.getFuzzySet();
			Set<String> fsString = fsFilter.getSupport();
			for (String s : fsString) {
				double max = Math.max(fsFilter.getValue(s), 1 - knowledgeThreshold);
				// if(fsFilter.getValue(s) < 1 - knowledgeThreshold){
				fsFilter.removeElement(s);
				fsFilter.setValue(s, max);
				// }
				// System.out.println(s+" "+fsFilter.getValue(s));
			}

		}

	}

}
