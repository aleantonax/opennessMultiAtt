package fif_learning.principles;



import java.io.IOException;

import fif_core.Metadata;

public interface Principle {
	
	public void apply(Metadata mResource, Metadata mFilter) throws IOException;

}
