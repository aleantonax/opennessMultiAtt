package fif_learning.principles.chain;

import java.util.AbstractList;
import fif_learning.FilterUpdate;
import fif_learning.principles.*;

public class SpaRelHandler extends PrincipleHandler{

public void handleRequest(String relevanceChoice, AbstractList<Principle> pList) {
	
	
	
	String spatialFlag = FilterUpdate.userConfigMap.get("spatial");
		
		if (relevanceChoice.contains("spatial") && spatialFlag.equalsIgnoreCase("true")){
			pList.add(new Spatial());
		}

		if (successor != null) {
			successor.handleRequest(relevanceChoice, pList);
		}
	}

}
