package openness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fif_core.Attribute;
import fif_possibilisticLearning.utility.fileHandler;
import fif_possibilisticLearning.utility.fileWriter;
import openness.data_server.OpennessSqlDataServer;
import openness.data_server.OpennessSqlDataServerRegister;
import openness.data_server.TransactionResourceBuilder;
import openness.utility.XMLReader;
import provider.Provider;
import provider.ResourceProvider;
import provider.exceptions.ConnectionFailException;
import provider.exceptions.ServerAlreadyExistException;
import provider.exceptions.UnavailableServerException;
import provider.registration.DataServerRegistration;
import provider.resourceBuilder.FifResource;

/**
 * Classe contenente il metodo main. Manipola le risorse FIF come indicato nel
 * file xml OpennessReadConf.xml e le aggiorna.
 * 
 * @author Alessandro Antonacci
 * 
 *
 */
public class OpennessResourceManager {
	// ogni utente ha il suo updater
	private HashMap<String, UserFilterUpdater> userFilterUpdaters = new HashMap<String, UserFilterUpdater>();
	private DataServerRegistration sqlEng = null;

	public OpennessResourceManager() {
		if (sqlEng == null)
			// crea il registro dal quale poi verrà prelevato il dataserver dal
			// provider
			sqlEng = new OpennessSqlDataServerRegister();
	}

	/*
	 * logga il server(viene inserito nell'hashmap string-dataserver),ed
	 * effettua lettura-aggiornamento filtri
	 */
	/**
	 * effettua l'aggiornamento dei filtri multiattributo. utilizza i due file
	 * xml OpennessConnConf.xml per leggere i parametri di connessione al
	 * database e OpennessReadConf.xml per la lettura dei dati.
	 * 
	 * 
	 */
	public void updateUserFilters() {

		try {
			// lettore per file xml riguardante la connessione
			XMLReader readerConnConf = new XMLReader("\\openness_config\\OpennessConnConf.xml", "OpennessConnConf");

			// lettore per file xml riguardante la lettura
			XMLReader readerReadConf = new XMLReader("\\openness_config\\OpennessReadConf.xml", "OpennessReadConf");
			// fine modifica antonacci

			// legge dall'xml i parametri per la connessione e la lettura dal db
			String driver = readerConnConf.getConfigurationParameter("driver");
			String connectionUrl = readerConnConf.getConfigurationParameter("connectionUrl");

			String attributes = readerReadConf.getConfigurationParameter("attribute");
			String table = readerReadConf.getConfigurationParameter("table");

			// id risorsa
			String idRisorsa = readerReadConf.getConfigurationParameter("id");

			// se c'è più di uno userId,saranno separati da una virgola
			HashMap<String, ArrayList<String>> usersAndTheirAttributes = readerReadConf.getUsersFuzzyAttributes("user",
					"userId", "fuzzy");

			// per ogni utente...
			for (String user : usersAndTheirAttributes.keySet()) {
				UserFilterUpdater.filters += "\n******************* user: " + user;

				// crea filtro e lo aggiorna

				/*
				 * per ogni attributo su cui calcolare fuzzyset,aggiunge un
				 * attribute alla classe transaction resource builder. Le
				 * risorse così formate avrano un metadato per ciascun attributo
				 */
				for (String attr : usersAndTheirAttributes.get(user))
					TransactionResourceBuilder.FUZZY_ATT.add(new Attribute(attr));

				String fuzzy = usersAndTheirAttributes.get(user).toString().replace(" ", "").replace("[", "")
						.replace("]", "");

				try {
					sqlEng.logIN();
				} catch (ServerAlreadyExistException e1) {
					System.err.println("Looks like there's already a logged in server with that given name.");
				}
				Provider<FifResource> resProvider = new ResourceProvider<FifResource>(
						OpennessSqlDataServerRegister.SERVER_NAME + ":: " + OpennessSqlDataServer.DRIVER + " " + driver
								+ " " + OpennessSqlDataServer.CONNECTION_URL + " " + connectionUrl + " "
								+ OpennessSqlDataServer.ATTRIBUTES + " " + attributes + " "
								+ OpennessSqlDataServer.TABLE + " " + table + " " + OpennessSqlDataServer.ID + " "
								+ idRisorsa + " " + OpennessSqlDataServer.FUZZY + " " + fuzzy + " "
								+ OpennessSqlDataServer.USER_ID + " " + user);

				/*
				 * itera sulle risorse l'iteratore costruisce sulle transazioni
				 * costruite dalla richiesta al db,delle risorse fif
				 */
				Iterator<FifResource> ait = resProvider.iterator();
				int maxResources = 0;
				while (ait.hasNext() && maxResources < 10) {
					FifResource tempRes = ait.next();
					maxResources++;
					if (maxResources > 0) {
						String resourceUri = tempRes.getResourceName().getUri().toString();

						UserFilterUpdater userFilter = userFilterUpdaters.get(user);
						/*
						 * se non c'è uno userFilterUpdater per l'utente con
						 * quell'id ne crea uno
						 */
						if (userFilter == null) {
							userFilter = new UserFilterUpdater(null);
							// così facendo crea due filtri diversi,uno per
							// ciascun utente
							userFilterUpdaters.put(user, userFilter);
						}
						userFilter.addIdResIdUser(resourceUri, user);
						userFilter.updateUserFilter(tempRes);
					}
				}
				// ripulisce transactionresourcebuilder.fuzzyatt per gli altri
				// utenti
				TransactionResourceBuilder.FUZZY_ATT.clear();
				UserFilterUpdater.filters += "\n*******************";

				sqlEng.logOUT();
			}
		} catch (UnavailableServerException | ConnectionFailException e) {
			System.err.println("Errore nella connessione: il server non è disponibile.");
		}
	}

	public static void main(String[] args) throws IOException {
		OpennessResourceManager erm = new OpennessResourceManager();
		erm.updateUserFilters();
		System.out.println(UserFilterUpdater.filters);

		/*
		 * scrittura dei filtri su un file txt,per il testing. Stampa i filtri
		 * di ogni metadato per ogni utente,ad ogni iterazione
		 */
		fileHandler f = new fileWriter("Filters.txt");
		f.openFile();
		f.writeFile(UserFilterUpdater.filters);
		f.closeFile();
	}
}