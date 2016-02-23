package fif_core.exceptions;

/**This exception is launched when you try to aggregate a number of values not corresponding to number of weights.<br><br>
 * 
 * @author Troiano Lorenzo
 * @version 1.0
 */


public class IllegalNumerOfValuesException extends Exception {


	private static final long serialVersionUID = 1L;

	public IllegalNumerOfValuesException() {
		
		super("The number of values of weights is not corresponding to the number of values you want aggregate.");
	}

	

}
