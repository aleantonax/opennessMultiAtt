package openness.utility;

import java.util.HashMap;
import java.util.Map;

public class P_CareTaker {
	private Map<String, P_Memento> mementoMap = new HashMap<String, P_Memento>();

	// aggiunge una voce alla mappa degli stati del filtro. Se è già presente un
	// metadato con uno statoesso viene aggiornato
	public void add(String desc, P_Memento state) {
		if (mementoMap.keySet().contains(desc)) {
			mementoMap.remove(desc);
			mementoMap.put(desc, state);
		} else
			mementoMap.put(desc, state);
	}

	// restituisce una stringa contenente una rappresentazione della mappa
	// descrizione-stato di memento Map (utilizzata per verificare i cambiamenti
	// dello stato del filtro)
	public String get() {
		String res = "";
		for (String k : mementoMap.keySet()) {
			res += k + " " + mementoMap.get(k).getState() + "\n";
		}
		return res;
	}

}
