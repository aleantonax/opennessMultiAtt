package fif_learning.interpretation_chain;

import java.io.IOException;
import java.util.AbstractList;
import fif_core.Metadata;
import fif_core.PossibilisticInterpretation;
import fif_core.interfaces.Interpretation;
import fif_learning.principles.*;

public class PossibilisticInterpretationHandler extends InterpretationHandler {

	@Override
	public void handleRequest(Metadata mResource, Metadata mFilter) throws IOException {
		/*
		 * Rimozione Centrone 608758 ArrayList<String> principleSelected = new
		 * ArrayList<String>();
		 * 
		 */
		Interpretation i = mResource.getInterpretation();
		System.out.println("\nPossibilistic Interpretation Handler: Resource fuzzy Set : " + mResource.getFuzzySet().getHash());
		if (i instanceof PossibilisticInterpretation) {
			PrincipleFactory p = new PrincipleFactory();

			// restituisce la lista dei principi da applicare(presi nella classe
			// principle factory dal file di configurazione config-properties)
			AbstractList<Principle> pList = p.createPrinciple();

			for (Principle pr : pList) {

				// istanzia PrincipleContext con il principio da applicare preso
				// da pList
				PrincipleContext pContext = new PrincipleContext(pr);

				// applica il principio in questione passandogli i metadati su
				// cui lavorare
				pContext.applyPrinciple(mResource, mFilter);
				if (pr instanceof Temporal || pr instanceof Spatial || pr instanceof Observation) {
					System.out.println("Possibilistic Interpretation Handler: Resource fuzzy Set : " + mResource.getFuzzySet().getHash());
				} else if (pr instanceof Knowledge) {
					System.out.println("Possibilistic Interpretation Handler: Filter fuzzy Set : " + mFilter.getFuzzySet().getHash());
				}

				/*
				 * Rimozione Centrone 608758 if (pr instanceof Temporal) {
				 * principleSelected.add("temporal"); System.out.println(
				 * "Resource fuzzy Set : "+mResource.getFuzzySet().getHash()); }
				 * else if (pr instanceof Spatial) {
				 * principleSelected.add("spatial"); System.out.println(
				 * "Resource fuzzy Set : "+mResource.getFuzzySet().getHash()); }
				 * else if (pr instanceof Observation) {
				 * principleSelected.add("observation"); System.out.println(
				 * "Resource fuzzy Set : "+mResource.getFuzzySet().getHash()); }
				 * else if (pr instanceof Knowledge) {
				 * principleSelected.add("knowledge"); System.out.println(
				 * "Filter fuzzy Set : "+mFilter.getFuzzySet().getHash()); }
				 */
			}
			/*
			 * Aggiunta Centrone 608758
			 */
			// chiama il metodo minposrelevance della classe base relevance
			// criteria (applica principio di minima specificità tra filtro e
			// risorsa (metadati))
			mFilter.setFuzzySet(BaseRelevanceCriteria.minPosRelevance(new Metadata(mResource.getAttribute(),
					mResource.getFuzzySet(), PossibilisticInterpretation.getinstance()), mFilter));
			/*
			 * Fine Aggiunta Centrone 608758
			 */
			/*
			 * Rimozione Centrone 608758
			 */
			/*
			 * if (!principleSelected.isEmpty()) { for (String s :
			 * principleSelected) { switch (s) { case "temporal":
			 * System.out.println("\nBASE RELEVANCE TEMPORAL"); fsFilter =
			 * BaseRelevanceCriteria .minPosRelevance( new Metadata(
			 * mResource.getAttribute(),
			 * FuzzySet.createFuzzySet(FilterUpdate.memorizedData),
			 * PossibilisticInterpretation .getinstance()), mFilter); break;
			 * 
			 * case "spatial": System.out.println("\nBASE RELEVANCE SPATIAL");
			 * fsFilter = BaseRelevanceCriteria.minPosRelevance( new
			 * Metadata(mResource.getAttribute(), FuzzySet
			 * .createFuzzySet(FilterUpdate.aj), PossibilisticInterpretation
			 * .getinstance()), mFilter); break;
			 * 
			 * case "observation": System.out.println(
			 * "\nBASE RELEVANCE OBSERVATION"); fsFilter =
			 * BaseRelevanceCriteria.minPosRelevance( mResource, mFilter);
			 * break;
			 * 
			 * case "knowledge": System.out.println("BASE RELEVANCE KNOWLEDGE");
			 * fsFilter = BaseRelevanceCriteria.minPosRelevance( mResource,
			 * mFilter); break; } } }
			 */
			/*
			 * Fine rimozione Centrone 608758
			 */

		} else if (successor != null) {
			successor.handleRequest(mResource, mFilter);
		}

	}
}
