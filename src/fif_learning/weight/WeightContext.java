package fif_learning.weight;

import fif_core.interfaces.Aggregator;

public class WeightContext {


	private Weights weight;

	public WeightContext(Weights weight) {
		this.weight = weight;
	}


	public Aggregator matchWeight(int n) {
		return this.weight.matchWeight(n);
	}


}
