package fif_learning.interpretation_chain;

import java.io.IOException;

import fif_core.ClosedVeristicInterpretation;
import fif_core.Metadata;
import fif_core.interfaces.Interpretation;

public class ClosedVeristicInterpretationHandler extends InterpretationHandler{
	
	@Override
	public void handleRequest(Metadata mResource, Metadata mFilter) throws IOException {
		
		Interpretation i = mResource.getInterpretation();
		
		if (i instanceof ClosedVeristicInterpretation){
			//TODO corpo dell'if
			System.out.println("Closed Veristic Interpretation");
		} else if (successor != null) {
			successor.handleRequest(mResource, mFilter);
		}
	}
}
