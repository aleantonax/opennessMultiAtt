package openness.utility;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author Alessandro Antonacci
 * 
 */
// classe utility per la lettura del file di configurazione di openness
public class XMLReader {

	private String CONFIGURATION_FILE_PATH = System.getProperty("user.dir");
	// nodo radice del file xml da cui leggere i parametri
	private String ROOT_NODE;

	// aggiunta Antonacci
	public XMLReader(String confFilePath, String rootNode) {

		CONFIGURATION_FILE_PATH += confFilePath;
		ROOT_NODE = rootNode;

	}

	/**
	 * preleva i parametri di configurazione dal file xml associati a uno
	 * specifico tag,passato come parametro. Nel caso ci siano più tag con lo
	 * stesso nome passato come parametro(come ad esempio più tag "p" in una
	 * pagina web) verranno restituiti i valori contenuti in ciascun
	 * tag,separati da una virgola
	 * 
	 * @param tagName
	 *            il nome del tag di cui prelevare il valore
	 * @return una stringa contenente il valore o i valori del tag passato come
	 *         parametro
	 */

	// preleva i parametri di configurazione per la connessione al db o per la
	// lettura dal db corrispondenti al tag passato come parametro
	public String getConfigurationParameter(String tagName) {

		String toReturn = null;

		File file = new File(CONFIGURATION_FILE_PATH);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc;

			doc = dBuilder.parse(file);

			doc.getDocumentElement().normalize();
			System.out.println("XMLReader -----------------------------------\n Reading xml...");
			System.out.println("\n Root element: " + doc.getDocumentElement().getNodeName());

			// preleva il nodo radice. Questo metodo preleva tutti i nodi (una
			// lista) con quel nome.
			// Utile per prelevare tutti i nodi col nome attribute
			NodeList nList = doc.getElementsByTagName(ROOT_NODE);

			Node nNode = nList.item(0);

			// se la lista di nodi dato il nome del tag contiene un solo
			// nodo o più nodi,usa get list tag value che preleva o un
			// SINGOLO valore di un nodo
			// o più. Utile quando ci sono più occorrenze di un tag (ad esempio
			// più tag attribute)
			// e comunque utilizzato per i parametri singoli per la connessione
			// al db
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				toReturn = getListTagValue(tagName, eElement);
			}
		} catch (ParserConfigurationException e) {
			throw new XMLException("Parse Error in XML configuration");
		} catch (SAXException e) {
			throw new XMLException("Parse Error in XML configuration");
		} catch (IOException e) {
			throw new XMLException("XML Configuration file Exception: it might not exist");
		} catch (Exception e) {
			throw new XMLException("XML Configuration file Exception: Parameter node might not exist");
		}

		return toReturn;
	}

	// aggiunta antonacci
	// preleva una stringa contenente un insieme di valori del nodo identificato
	// da sTag figlio del nodo
	// element(utile in presenza di più tag "attribute")
	private String getListTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag);

		// gli attributi del file xml da restituire
		ArrayList<String> attributes = new ArrayList<String>();

		for (int i = 0; i < nlList.getLength(); i++) {
			Node nValue = nlList.item(i).getChildNodes().item(0);
			attributes.add(" " + nValue.getNodeValue());
		}

		// elimina le parentesi quadre,e genera una stringa con tutti i valori
		// trovati,separati da una virgola (utile per attributes e
		// attributesforfuzzy). se trova un solo valore,sarà inserito nella
		// stringa solo quel valore senza la virgola
		String result = attributes.toString();
		result = result.replace("[", "");
		result = result.replace("]", "");
		result = result.replace(" ", "");

		result = result.trim();

		// feedback
		System.out.println(
				"\n Parameters found in XML file in " + CONFIGURATION_FILE_PATH + ": \n" + sTag + ": " + result + "\n");

		return result;

	}



	/**
	 * restituisce un hashmap contenente al suo interno la configurazione di
	 * lettura per ciascun utente.
	 * 
	 * @param tagUser
	 *            il tag che identifica l'utente
	 * @param tagId
	 *            il tag che identifica univocamente un utente
	 * @param tagProperty
	 *            il tag che indica la proprietà da prelevare,per ciascun utente
	 * 
	 * @return un hashmap contenente,per ciascun valore di tagId,una lista di
	 *         valori di tagProperty
	 * 
	 */
	public HashMap<String, ArrayList<String>> getUsersFuzzyAttributes(String tagUser, String tagId,
			String tagProperty) {

		SAXBuilder bldr = new SAXBuilder();
		File xmlFile = new File(CONFIGURATION_FILE_PATH);
		HashMap<String, ArrayList<String>> map = new HashMap<>();
		ArrayList<String> userFuzzyAttribtes = null;
		try {

			org.jdom2.Document document = (org.jdom2.Document) bldr.build(xmlFile);
			org.jdom2.Element rootNode = document.getRootElement();
			// preleva i nodi utente
			List<org.jdom2.Element> list = rootNode.getChildren(tagUser);
			// per ogni nodo utente...
			for (int i = 0; i < list.size(); i++) {

				org.jdom2.Element node = (org.jdom2.Element) list.get(i);
				// per ogni utente preleva il nodo config
				List<org.jdom2.Element> nList = node.getChildren(tagProperty);
				userFuzzyAttribtes = new ArrayList<>();

				for (int m = 0; m < nList.size(); m++) {
					// per ogni nodo figlio di config (spatial,ecc) preleva nome
					// del nodo e valore
					// System.out.println(nList.get(m).getValue());

					userFuzzyAttribtes.add(nList.get(m).getValue());
				}
				map.put(node.getChild(tagId).getValue(), userFuzzyAttribtes);
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return map;
	}

	/**
	 * restituisce un hashmap contenente,per ciascun utente,una mappa contenente
	 * la sua configurazione di aggiornamento filtri
	 * 
	 * @param tagUser
	 *            il tag che identifica l'utente
	 * @param tagId
	 *            il tag che identifica univocamente un utente
	 * @param tagProperty
	 *            il tag che indica la proprietà da prelevare,per ciascun utente
	 * @return una mappa contenente, per ciascun utente, una mappa contenente la
	 *         sua configurazione di aggiornamento filtri
	 */
	// restituisce per un hashmap con ciascun utente e un'hashmap contenente la
	// sua configurazione di aggiornamento (se utilizza spatial o altri
	// principi e le threshold)
	public HashMap<String, HashMap<String, String>> getUsersConfig(String tagUser, String tagId, String tagProperty) {
		SAXBuilder bldr = new SAXBuilder();
		File xmlFile = new File(CONFIGURATION_FILE_PATH);
		HashMap<String, HashMap<String, String>> map = new HashMap<>();
		HashMap<String, String> configValues = null;
		try {

			org.jdom2.Document document = (org.jdom2.Document) bldr.build(xmlFile);
			org.jdom2.Element rootNode = document.getRootElement();
			// preleva i nodi utente
			List<org.jdom2.Element> list = rootNode.getChildren(tagUser);
			// per ogni nodo utente...
			for (int i = 0; i < list.size(); i++) {

				org.jdom2.Element node = (org.jdom2.Element) list.get(i);
				// per ogni utente preleva il nodo config
				List<org.jdom2.Element> nList = node.getChildren(tagProperty);
				for (int m = 0; m < nList.size(); m++) {
					configValues = new HashMap<>();
					// per ogni nodo figlio di config (spatial,ecc) preleva nome
					// del nodo e valore
					List<org.jdom2.Element> l = nList.get(m).getChildren();
					for (org.jdom2.Element el : l) {
						// System.out.println(el.getName() + " " +
						// el.getValue());
						configValues.put(el.getName(), el.getValue());
					}
					// System.out.println(configValues);
					// System.out.println(nList.get(m).getChildren().get(m).getName()
					// + ": "
					// + nList.get(m).getChildren().get(m).getValue());
					map.put(node.getChild(tagId).getValue(), configValues);

				}

			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return map;
	}

//	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//		XMLReader reader = new XMLReader("\\openness_config\\OpennessReadConf.xml", "OpennessReadConf");
//		// System.out.println(reader.getConfigurationParameter("userId"));
//		// System.out.println((reader.getSecondLevelContents()));
//		HashMap<String, HashMap<String, String>> map = reader.getUsersConfig("user", "userId", "config");
//		System.out.println(map);
//		System.out.println(map.get("46341").get("filterthreshold"));
//		System.out.println(reader.getUsersFuzzyAttributes("user", "userId", "fuzzy"));
//
//	}

}
