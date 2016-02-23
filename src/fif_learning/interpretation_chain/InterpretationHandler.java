package fif_learning.interpretation_chain;

import java.io.IOException;

import fif_core.Metadata;

/**
 * Offers the possibility to implement the Chain of Responsibility pattern in order to
 * handle the interpreatation requests.
 * @author Gianmarco Divittorio
 * @author Nicola Gazzilli
 *
 */
public abstract class InterpretationHandler {
	
	protected InterpretationHandler successor;
	 
    public void setSuccessor(InterpretationHandler successor) {
        this.successor = successor;
    }
 
    abstract public void handleRequest(Metadata mResource, Metadata mFilter) throws IOException;

}
