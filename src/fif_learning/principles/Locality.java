package fif_learning.principles;

import java.io.IOException;
import fif_core.Metadata;

public abstract class Locality implements Principle{
	
	public abstract void apply(Metadata mResource, Metadata mFilter) throws IOException;	

}
