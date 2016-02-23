package fif_learning.principles.chain;

import java.util.AbstractList;
import fif_learning.FilterUpdate;
import fif_learning.principles.*;


public class KnoRelHandler extends PrincipleHandler {

	public void handleRequest(String relevanceChoice, AbstractList<Principle> pList) {
		

		
		String knowledgeFlag = FilterUpdate.userConfigMap.get("knowledge");
		
		if (relevanceChoice.contains("knowledge") && knowledgeFlag.equalsIgnoreCase("true")){
			pList.add(new Knowledge());
		}

		if (successor != null) {
			successor.handleRequest(relevanceChoice, pList);
		}
	}
}
