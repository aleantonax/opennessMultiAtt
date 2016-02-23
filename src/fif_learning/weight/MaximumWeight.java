package fif_learning.weight;

import fif_core.OWA;
import fif_core.interfaces.Aggregator;

/**
 * Implements the Weights interface, offering the possibility to create Maximum
 * OWA weights. A Maximum OWA weight is a discriminating item in the process of
 * creation of parallel filters.
 * 
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public class MaximumWeight implements Weights {

	/**
	 * Takes an array of filters in input returning an array of double values.
	 * Each of the double values coincides with the item in the same position of
	 * the filters' array and resembles its unique weight.
	 * 
	 * @param f
	 *            - the array of filters
	 * @return weight - the array of corresponding weights
	 */
	@Override
	public Aggregator matchWeight(int n) {

		assert (n > 0) : "FilterCreation: the filter array has null value.";

		double[] weight = new double[n];

		weight[0] = 1;
		for (int i = 1; i < weight.length; i++) {
			weight[i] = 0;
		}

		Aggregator a = new OWA(weight);

		return a;

	}

}
