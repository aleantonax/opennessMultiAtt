package openness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import openness.data_server.TransactionResourceBuilder;
//import openness.utility.P_CareTaker;
//import openness.utility.P_Originator;
import openness.utility.XMLReader;
import provider.resourceBuilder.FifResource;
import fif_core.Attribute;
import fif_core.DescriptionBasedFilter;
import fif_core.Descriptor;
import fif_core.Filter;
import fif_core.FuzzySet;
import fif_core.Metadata;
import fif_core.ParallelFilter;
import fif_core.PossibilisticInterpretation;
import fif_core.ResourceRegister;
import fif_core.SequenceFilter;
import fif_core.exceptions.MetadataWithSameAttributeException;
import fif_learning.FilterUpdate;

public class UserFilterUpdater {
	private ArrayList<Filter> userFilters = new ArrayList<>();
	// mappa contenente gli id risorsa mappati con l'id utente. utilizzato per
	// capire di ciascuna risorsa,come effettuare l'aggiornamento (sul file xml
	// ogni utente (id utente) ha i suoi parametri di aggiornamento)
	private HashMap<String, String> idResUserId = new HashMap<>();
	// una rappresentazione sottoforma di stringa dei filtri utente
	static String filters = "";

	// design pattern memento,serve a tener traccia del cambiamento dello stato
	// dei
	// filtri
	// private P_Originator or = new P_Originator();
	// private P_CareTaker ct = new P_CareTaker();

	public UserFilterUpdater(ArrayList<Filter> userFilters) {

		if (userFilters == null) {
			// se non ha conoscenza pregressa,inizializza i filtri
			this.userFilters = createListNewEmptyFilter();
		} else
			this.userFilters = userFilters;
	}

	/**
	 * restituisce i filtri basati su descrizione
	 * 
	 * @return una lista di filtri fuzzy
	 */
	public ArrayList<Filter> getUserFilters() {
		return userFilters;
	}

	/**
	 * aggiorna il filtro utente con la risorsa passata come parametro
	 * 
	 * @param r
	 *            FifResource utilizzata per l'aggiornamento del filtro
	 */
	public void updateUserFilter(FifResource r) {
		assert (r != null) : "Null parameter";

		updateFilters(userFilters, r);
		printFilters(userFilters);
	}

	/**
	 * Creates a new Filter
	 * 
	 * @return
	 */
	private ArrayList<Filter> createListNewEmptyFilter() {
		Descriptor completeDescriptor = new Descriptor();
		ArrayList<Metadata> metadata = new ArrayList<Metadata>();
		ArrayList<Filter> filters = new ArrayList<Filter>();

		metadata = this.getListFullMetadata();
		try {
			completeDescriptor.setMetadata(metadata);
		} catch (MetadataWithSameAttributeException e) {
			e.printStackTrace();
		}
		for (Metadata m : metadata) {
			filters.add(
					new DescriptionBasedFilter(completeDescriptor.getMetadata(m.getAttribute().getAttributeName())));

		}
		return filters;
	}

	// preleva la configurazione di aggiornamento filtri dato un utente
	private HashMap<String, String> readUserConfig(String user) {
		XMLReader reader = new XMLReader("\\openness_config\\OpennessReadConf.xml", "OpennessReadConf");
		// hashmap contenente tutti i parametri di configurazione,dato l'utente
		HashMap<String, String> userConfig = reader.getUsersConfig("user", "userId", "config").get(user);
		return userConfig;
	}

	private ArrayList<Filter> updateFilters(ArrayList<Filter> filters, FifResource r) {
		assert (filters != null) : "Null parameter";
		assert (r != null) : "Null parameter";

		// preleva l'id della risorsa e trova l'id utente di quella risorsa
		String resourceId = r.getResourceName().getUri().toString();
		String user = idResUserId.get(resourceId);

		// preleva la configurazione di aggiornamento filtri di quell'utente
		HashMap<String, String> userConfig = readUserConfig(user);

		ResourceRegister rr = ResourceRegister.getinstance();
		rr.associateDescriptor(r.getResourceName(), r.getDescriptor());
		try {
			for (int i = 0; i < filters.size(); i++) {
				filters.set(i, FilterUpdate.updaterWithConfig(rr, filters.get(i), userConfig));
				// or.setState(((DescriptionBasedFilter)
				// f).getMetadata().getFuzzySet().getHash().toString());
				// ct.add(((DescriptionBasedFilter)
				// f).getMetadata().getAttribute().getAttributeName(),
				// or.saveStateToMemento());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		rr.removeAllAssocitions();

		return filters;
	}

	/**
	 * Prints the filter
	 * 
	 * @param f
	 *            filter to print
	 */
	private void printFilters(List<Filter> filters) {
		for (Filter filter : filters) {
			if (filter instanceof ParallelFilter) {
				System.out.println("parallelo");
				List<Filter> list = ((ParallelFilter) filter).getAllFilters();
				printFilters(list);

			}
			if (filter instanceof SequenceFilter) {
				List<Filter> list = ((SequenceFilter) filter).getAllFilters();
				printFilters(list);
			}
			if (filter instanceof DescriptionBasedFilter) {
				DescriptionBasedFilter fd = (DescriptionBasedFilter) filter;
				System.out.println("\nFILTER PRINT");
				String fPrint = fd.getMetadata().getFuzzySet().getHash().toString();
				System.out.println(fPrint);
				UserFilterUpdater.filters += "\n" + fPrint;
			}
		}
		UserFilterUpdater.filters += "\n";

	}

	// per ogni attributo,inizializza il metadato e il suo fuzzyset
	private ArrayList<Metadata> getListFullMetadata() {
		ArrayList<Metadata> metadata = new ArrayList<Metadata>();

		for (Attribute att : TransactionResourceBuilder.FUZZY_ATT) {
			FuzzySet fs = new FuzzySet();

			fs.setValue("default", 0);

			Metadata m = new Metadata(new Attribute(att.getAttributeName()), fs,
					PossibilisticInterpretation.getinstance());

			metadata.add(m);
		}
		return metadata;
	}

	// aggiunge alla mappa una risorsa con l'id dell'utente che l'ha generata
	void addIdResIdUser(String idRes, String idUser) {
		idResUserId.put(idRes, idUser);
		// System.out.println(idResUserId);
	}
}