package fif_learning;
/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 * @version 0.1
 */

import java.util.AbstractList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import fif_core.*;
import fif_core.interfaces.*;
import fif_learning.interpretation_chain.ClosedVeristicInterpretationHandler;
import fif_learning.interpretation_chain.OpenVeristicInterpretationHandler;
import fif_learning.interpretation_chain.PossibilisticInterpretationHandler;
import fif_learning.weight.*;

/**
 * Provides several functionalities for the correct update of a filter.
 * 
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public abstract class FilterUpdate {

	public static Hashtable<String, Double> memorizedData = new Hashtable<String, Double>();
	public static Hashtable<String, Double> aj = new Hashtable<String, Double>();

	// ANTONACCI una map contenente tutti i valori di configurazione per
	// l'aggiornamento
	public static HashMap<String, String> userConfigMap = new HashMap<>();

	/**
	 * Given a filter and knowing its type (distinguishing between Description
	 * Based Filters, Parallel Filters and Sequence Filters), it is updated
	 * accordingly. More specifically, if f is a Sequence Filter, the method is
	 * called recursively on every filter that is part of f. If f is a Parallel
	 * Filter, the recursive call of the method is applied only on the filter
	 * with higher filtering grade among the filters that compose f. If f is a
	 * Description Based Filter, method has reached the base case and f will be
	 * updated according to the interpretation used in the building of the
	 * filter.
	 * 
	 * @param rr
	 * @param r
	 * @param f
	 * @throws Exception
	 */
	private static void updateFilter(ResourceRegister rr, Resource r, Filter f) throws Exception {

		assert (r != null) : "FilterCreation: the resource has null value.";
		assert (rr != null) : "FilterCreation: the register has null value.";

		// se è in parallelo aggiorna solo quello che contribuisce maggiormente
		// al filtraggio
		if (f instanceof ParallelFilter) {
			//System.err.println("è in parallelo");
			Filter fil = selectParallelFilter(rr, r, (ParallelFilter) f);
			updateFilter(rr, r, fil);
		}

		// li aggiorna tutti
		if (f instanceof SequenceFilter) {
		//	System.err.println("è in sequenza");
			AbstractList<Filter> filter = ((SequenceFilter) f).getAllFilters();
			for (Filter fil : filter) {
				updateFilter(rr, r, fil);
			}
		}

		// filtro elementare,la cui combinazione in serie o in parallelo crea
		// filtri complessi
		if (f instanceof DescriptionBasedFilter) {
			// preleva il metadato della risorsa in comune col metadato del
			// filtro
			Metadata mResource = extractMetadata(rr, r, (DescriptionBasedFilter) f);

			if (mResource != null) {
				// preleva il metadato del filtro
				Metadata mFilter = ((DescriptionBasedFilter) f).getMetadata();

				PossibilisticInterpretationHandler possibilistic = new PossibilisticInterpretationHandler();
				ClosedVeristicInterpretationHandler closedVeristic = new ClosedVeristicInterpretationHandler();
				OpenVeristicInterpretationHandler openVeristic = new OpenVeristicInterpretationHandler();

				// QUI AVVIENE L'UPDATE:chain of responsability
				possibilistic.setSuccessor(closedVeristic);
				closedVeristic.setSuccessor(openVeristic);
				// interpreta il filtro in modo possibilistico
				possibilistic.handleRequest(mResource, mFilter);
			}
		}
	}

	/**
	 * If the filtering grade between a resource and a filter doesn't surpass a
	 * specific input-defined threshold, the filter is then put in a Parallel
	 * Filter along with the filter created using the resource, during the
	 * update phase. Otherwise, the filter is updated as usual.
	 * 
	 * @param rr
	 * @param r
	 * @param f
	 * @return
	 * @throws Exception
	 */
	
	public static Filter updaterWithConfig(ResourceRegister rr, Filter f, HashMap<String, String> userConfig)
			throws Exception {

		userConfigMap = userConfig;
		//System.out.println(userConfigMap);
		/*
		 * Aggiunta Centrone 608758
		 */
		FilterUpdate.memorizedData = f.getMemorizedData();
		// FilterUpdate.aj=f.getAj();
		/*
		 * fina aggiunta
		 */

		double threshold;
		threshold = Double.parseDouble(userConfig.get("filterthreshold"));

		assert (rr != null) : "FilterCreation: the register has null value.";
		assert (f != null) : "FilterCreation: the filter has null value.";

		// la lista di risorse del registro di risorse
		AbstractList<Resource> alResource = rr.getSequence();

	

		// se il matching supera la soglia...
		for (Resource r : alResource) {
			if (f.doFilter(r) >= threshold) {
//				System.out.println("Filter update***********************"
//						+ ((DescriptionBasedFilter) f).getMetadata().getFuzzySet().getHash());
						

				// aggiorna il filtro(fiflearning)
				updateFilter(rr, r, f);
			} else {
				// altrimenti se non supera la soglia lo mette in parallelo
				Filter newFilter = FilterCreation.createFilter(rr, r);
				WeightContext w = new WeightContext(new EqualWeight());
				Aggregator a = w.matchWeight(2);
				f = new ParallelFilter(a, newFilter, f);
			}
		}
		// files2.closeFile();
		// files.closeFile();
		return f;
	}

	/**
	 * Given a Parallel Filter and a Resource in input, selects the filter
	 * inside f which has the higher filtering * grade.
	 * 
	 * @param f
	 * @param rr
	 * @param r
	 * @return
	 * @throws Exception
	 */
	private static Filter selectParallelFilter(ResourceRegister rr, Resource r, ParallelFilter f) throws Exception {

		assert (r != null) : "FilterCreation: the resource has null value.";
		assert (rr != null) : "FilterCreation: the register has null value.";
		assert (f != null) : "FilterCreation: the filter has null value.";

		double maxWeight = 0;
		Filter filter = null;
		// Descriptor d = rr.getDescriptor(r);
		AbstractList<Filter> pf = f.getAllFilters();
		for (Filter fil : pf) {
			// if (checkAttribute(d,(DescriptionBasedFilter)fil) == true){
			double weight = fil.doFilter(r);
			if (weight > maxWeight) {
				maxWeight = weight;
				filter = fil;
			}
		}
		return filter;
	}

	/**
	 * Checks for the Attribute related to a Description Based Filter to be
	 * contained in a Descriptor.
	 * 
	 * @param d
	 *            - the descriptor to check
	 * @param f
	 *            - the filter from which the attribute is fetched
	 * @return true if the attribute is in descriptor, false otherwise
	 */
	// verifica che l'attributo di un filtro base sia contenuto nel descrittore
	public static boolean checkAttribute(Descriptor d, DescriptionBasedFilter f) {
		assert (d != null) : "FilterCreation: the Descriptor has null value.";
		assert (f != null) : "FilterCreation: the DescriptionBasedFilter has null value.";

		boolean found = false;

		AbstractList<Metadata> metaList = d.getAllMetadata();
		Metadata[] metaArrayDescriptor = new Metadata[metaList.size()];
		Attribute[] attributeArrayDescriptor = new Attribute[metaList.size()];
		int i = 0;
		for (Metadata m : metaList) {
			metaArrayDescriptor[i] = m;
			attributeArrayDescriptor[i] = m.getAttribute();
			i++;
		}

		Metadata filterMetadata = f.getMetadata();
		Attribute filterAttribute = filterMetadata.getAttribute();

		for (int j = 0; j < attributeArrayDescriptor.length; j++) {
			if (attributeArrayDescriptor[j].equals(filterAttribute)) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Fetches the Metadata contained in both the Resource r and the Description
	 * Based Filter f.
	 * 
	 * @param rr
	 * @param r
	 * @param f
	 * @return The metadata related to the filter
	 */
	// preleva i metadati in comune tra risorsa e filtro
	public static Metadata extractMetadata(ResourceRegister rr, Resource r, DescriptionBasedFilter f) {

		assert (r != null) : "FilterCreation: the resource has null value.";
		assert (rr != null) : "FilterCreation: the register has null value.";
		assert (f != null) : "FilterCreation: the filter has null value.";

		Descriptor d = rr.getDescriptor(r);
		// preleva tutti i metadati della risorsa
		AbstractList<Metadata> metaList = d.getAllMetadata();

		// preleva il metadato del filtro
		Metadata mFilter = f.getMetadata();
		// preleva l'attributo del metadato del filtro
		Attribute aFilter = mFilter.getAttribute();

		// per ogni metadato della risorsa controlla se l'aggettivo è uguale a
		// quello del filtro
		for (Metadata m : metaList) {
			Attribute aResource = m.getAttribute();
			if (aResource.equals(aFilter)) {
				return m;
			}
		}
		return null;
	}

	public static String getStringFromHashtable(Hashtable<String, Double> m) {
		String memorizedData = "";
		Set<String> ss = new HashSet<String>(m.keySet());
		// memorizedData+="";
		for (String s : ss) {
			memorizedData += s + ";" + m.get(s) + ";";
		}
		// memorizedData+="|";
		return memorizedData;
	}

	

	public static String getValueFromUserConfigMap(String attr) {

		return userConfigMap.get(attr);
	}

	

	public static FuzzySet createFuzzySet(Hashtable<String, Double> ht) {
		FuzzySet fs = new FuzzySet();
		Set<String> sss = new HashSet<String>(ht.keySet());
		for (String ss : sss) {
			fs.setValue(ss, (Double) ht.get(ss));
		}
		return fs;
	}

}