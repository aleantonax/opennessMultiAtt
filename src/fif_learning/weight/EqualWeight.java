package fif_learning.weight;

import fif_core.OWA;
import fif_core.interfaces.Aggregator;

/**
 * Implements the Weights interface, offering the possibility to create Equal OWA weights.
 * An Equal OWA weight is a discriminating item in the process of creation of parallel filters.
 * 
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public class EqualWeight implements Weights {

	
	/**
	 * Takes an array of filters in input returning an array of double values. 
	 * Each of the double values coincides with the item in the same position of the filters' array 
	 * Each of the filters will have the same weight.
	 * 
	 * @param f - the array of filters
	 * @return weight - the array of corresponding weights
	 */
	public Aggregator matchWeight(int n){
		
		assert (n > 0)  : "FilterCreation: error value .";
		
		double[] weights = new double[n];
		for( int j = 0; j < n; j++){
			weights[j] = (double)1/n;
		}
		
		Aggregator a = new OWA(weights);

		return a;
	}


}
