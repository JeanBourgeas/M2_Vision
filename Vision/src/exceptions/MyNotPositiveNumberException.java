package exceptions;

/**
 * Use this Exception when an integer should be positive but are not.
 * @author Jean
 *
 */
public class MyNotPositiveNumberException extends MyExceptions {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Exception : </br>
	 * "The number " + number + " is not positive."
	 * @param number (int) The value of the integer.
	 */
	public MyNotPositiveNumberException(int number) {
		System.err.println("The number " + number + " is not positive.");
	}

	/**
	 * Exception : </br>
	 * name + " = " + number + " is not positive."
	 * @param name (String)	The name of the integer.
	 * @param number (int) The value of the integer.
	 */
	public MyNotPositiveNumberException(String name, int number) {
		System.err.println(name + " = " + number + " is not positive.");
	}
	
	/**
	 * Use this to test if an integer is positive and throw the exception if needed.
	 * @param name (String) The name of the integer.
	 * @param number (int) The value of the integer.
	 * @throws MyNotPositiveNumberException
	 */
	public static void test(String name, int number) throws MyNotPositiveNumberException {
		if(number < 0)
			throw new MyNotPositiveNumberException(name, number);
	}
}
