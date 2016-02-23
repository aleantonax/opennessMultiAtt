package fif_learning.principles;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.Properties;

import fif_core.CheckedLinkedList;
import fif_learning.FilterUpdate;
import fif_learning.principles.chain.*;


public class PrincipleFactory {
	
	public AbstractList<Principle> pList = new CheckedLinkedList<Principle>();

	public AbstractList<Principle> createPrinciple(){
		
		Properties prop = new Properties();
		String filename = "config.properties";
		InputStream input = FilterUpdate.class.getClassLoader().getResourceAsStream(filename);
		try {
			prop.load(input);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		String firstRelevanceChoice = (prop.getProperty("FIRST PRINCIPLE")).toLowerCase();
		String secondRelevanceChoice = (prop.getProperty("SECOND PRINCIPLE")).toLowerCase();
		String thirdRelevanceChoice = (prop.getProperty("THIRD PRINCIPLE")).toLowerCase();
		String lastRelevanceChoice = (prop.getProperty("LAST PRINCIPLE")).toLowerCase();
		
		KnoRelHandler knowledge = new KnoRelHandler();
		ObsRelHandler observation = new ObsRelHandler();
		TempRelHandler temporal = new TempRelHandler();
		SpaRelHandler spatial = new SpaRelHandler();
		

		knowledge.setSuccessor(observation);
		observation.setSuccessor(temporal);
		temporal.setSuccessor(spatial);

		// Il motivo per cui ci sono 4 istruzioni uguali con primo parametro diverso 
		// è dovuto al fatto che se vi sono variazioni nel file config si considera
		// solo l'istruzione il cui primo parametro è proprio il primo della lista 
		// di principi elencata nel file di configurazione. Quindi solo una delle seguenti
		// istruzioni verrà eseguita.
		knowledge.handleRequest(firstRelevanceChoice, pList);
		knowledge.handleRequest(secondRelevanceChoice, pList);
		knowledge.handleRequest(thirdRelevanceChoice, pList);
		knowledge.handleRequest(lastRelevanceChoice, pList);
	
		
		/*String knowledgeFlag = (prop.getProperty("KNOWLEDGE FLAG")).toLowerCase();
		String observationFlag = (prop.getProperty("OBSERVATION FLAG")).toLowerCase();
		String temporalFlag = (prop.getProperty("TEMPORAL FLAG")).toLowerCase();*/
		
		
		
		
		/*if (observationFlag.contains("on")){
			pList.add(new Observation());
			
		} 
		
		if (knowledgeFlag.contains("on")){
			pList.add(new Knowledge());
		}
		
		if(temporalFlag.contains("on")){
			pList.add(new Temporal());
		}*/
				
		return pList;
		
	}

}
