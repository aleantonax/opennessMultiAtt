package fif_learning.principles.chain;

import java.util.AbstractList;
import fif_learning.FilterUpdate;
import fif_learning.principles.*;


public class TempRelHandler extends PrincipleHandler{

public void handleRequest(String relevanceChoice, AbstractList<Principle> pList) {
	

	
	String temporalFlag = FilterUpdate.userConfigMap.get("temporal");
		
		if (relevanceChoice.contains("temporal") && temporalFlag.equalsIgnoreCase("true")){
			pList.add(new Temporal());
		}

		if (successor != null) {
			successor.handleRequest(relevanceChoice, pList);
		}
	}

}
