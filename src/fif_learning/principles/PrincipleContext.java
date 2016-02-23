package fif_learning.principles;

import java.io.IOException;

import fif_core.Metadata;

public class PrincipleContext {
	
	private Principle principle;
	
	public PrincipleContext(Principle p){
		principle = p;
	}
	
	public void applyPrinciple(Metadata mResource, Metadata mFilter) throws IOException{
		this.principle.apply(mResource, mFilter);	
	}

}
