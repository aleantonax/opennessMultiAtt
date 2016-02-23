package fif_learning;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;



import java.util.Properties;

import fif_core.*;
import fif_core.interfaces.*;
import fif_learning.weight.EqualWeight;
import fif_learning.weight.WeightContext;

/**
 * Allows to create filters from a Resource object.
 * 
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public class FilterCreation {
	
	
	Properties prop = new Properties();
	
	
	
	
		/*
		 * metodo che dato un ResourceRegister rr e una Resource r, estrae i metadati
		 * da rr con risorsa r, e crea una lista di filtri con i metadati
		 */
	
	

	/**
	 * Creates new filter basing the result on the input resources.
	 * There are two main possibilities:
	 * First one is having to deal with only one metadata passed in input. In this case, a DescriptionBasedFilter object will be returned.
	 * Second one is having to deal with multiple metadatas. A composite filter is then returned based on the
	 * value of a flag. If it is true, a SequenceFilter object is then returned, else a ParallelFilter object will be returned.
	 * @param rr
	 * @param r
	 * @return f - the filter created, based on the resource.
	 * @throws IOException 
	 */
	public static Filter createFilter(ResourceRegister rr, Resource r) throws IOException{
		
		assert (r!=null) : "FilterCreation: the resource has null value.";
		assert (rr!=null) : "FilterCreation: the register has null value.";
				
		
		Filter single_filter = null;
		Filter f = null;
			
		Descriptor g = rr.getDescriptor(r);
		
		AbstractList<Metadata> m = g.getAllMetadata();
		Filter[] filter_list = new Filter[m.size()];
		int i = 0;
		
		for (Metadata d : m){
			single_filter = new DescriptionBasedFilter(d);
			filter_list[i] = single_filter;
			i++;
		}
		
		//Se c'è un solo metadato crea un unico filtro base, se invece sono due o più crea un filtro sequenziale o parallelo a seconda della soglia
		if (filter_list.length == 1){
			f = single_filter;
			
		} else {
			
			String filename = "config.properties";
			InputStream input = FilterUpdate.class.getClassLoader().getResourceAsStream(filename);
			Properties prop = new Properties();
			prop.load(input);
			if(input == null){
				throw new FileNotFoundException("Property file not found.");
			}
			String flag = (prop.getProperty("FLAG SEQUENCE")).toLowerCase();
			
			
			//la scelta tra parallelo e sequenziale dipende dalla soglia
			if (flag.contains("true")){
				f = new SequenceFilter(filter_list);
				
			} else {				
				//imposta un peso equivalente a ciascun filtro
				WeightContext w = new WeightContext(new EqualWeight());
				Aggregator a = w.matchWeight(filter_list.length);
						
				f = new ParallelFilter(a,filter_list);
			}
		} 
		return f;
	}
}
