package fif_learning.principles.chain;

import java.util.AbstractList;

import fif_learning.principles.Principle;

public abstract class PrincipleHandler {
	
		
	protected PrincipleHandler successor;
	 
    public void setSuccessor(PrincipleHandler successor) {
        this.successor = successor;
    }
 
    abstract public void handleRequest(String relevanceChoice, AbstractList<Principle> pList);

}
