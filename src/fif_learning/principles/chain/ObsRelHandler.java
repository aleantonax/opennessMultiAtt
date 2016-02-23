package fif_learning.principles.chain;

import java.util.AbstractList;
import fif_learning.FilterUpdate;
import fif_learning.principles.*;



public class ObsRelHandler extends PrincipleHandler{
	
public void handleRequest(String relevanceChoice, AbstractList<Principle> pList) {
	
	
	String observationFlag = FilterUpdate.userConfigMap.get("observation");
		
		if (relevanceChoice.contains("observation") && observationFlag.equalsIgnoreCase("true")){
			pList.add(new Observation());
		}

		if (successor != null) {
			successor.handleRequest(relevanceChoice, pList);
		}
	}

}
