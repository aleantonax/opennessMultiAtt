package fif_core.interfaces;

import fif_core.exceptions.IllegalNumerOfValuesException;




public interface  Aggregator {
	
	
	public double aggregate(double... values) throws IllegalNumerOfValuesException ;

}
