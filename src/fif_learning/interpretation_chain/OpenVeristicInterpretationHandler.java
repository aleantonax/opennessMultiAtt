package fif_learning.interpretation_chain;

import java.io.IOException;

import fif_core.Metadata;
import fif_core.OpenVeristicInterpretation;
import fif_core.interfaces.Interpretation;

public class OpenVeristicInterpretationHandler extends InterpretationHandler{

	@Override
	public void handleRequest(Metadata mResource, Metadata mFilter) throws IOException {
		
		Interpretation i = mResource.getInterpretation();
		
		if (i instanceof OpenVeristicInterpretation){
			//TODO
			System.out.println("Open Veristic Interpretation");
		} else if (successor != null) {
			successor.handleRequest(mResource, mFilter);
		}

	}

}





