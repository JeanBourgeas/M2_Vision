package exceptions;

/**
 * Use this Exception when an integer should be an odd number but are not.
 * @author Jean
 *
 */
public class MyNotOddNumberException extends MyExceptions {
	private static final long serialVersionUID = 1L;

	/**
	 * Exception : </br>
	 * "The number is not an odd number."
	 */
	public MyNotOddNumberException() {
		System.err.println("The number is not an odd number.");
	}

	/**
	 * Exception : </br>
	 * name + " is not an odd number"
	 * @param name (String)	The name of the integer.
	 */
	public MyNotOddNumberException(String name) {
		System.err.println(name + " is not an odd number");
	}
	
	/**
	 * Exception : </br>
	 * "The number " + number + " is not an odd number."
	 * @param number (int) The value of the integer.
	 */
	public MyNotOddNumberException(int number) {
		System.err.println("The number " + number + " is not an odd number.");
	}

	/**
	 * Exception : </br>
	 * name + " = " + number + " is not an odd number."
	 * @param name (String)	The name of the integer.
	 * @param number (int) The value of the integer.
	 */
	public MyNotOddNumberException(String name, int number) {
		System.err.println(name + " = " + number + " is not an odd number.");
	}
	
	/**
	 * Use this to test if an integer is an odd number and throw the exception if needed.
	 * @param name (String) The name of the integer.
	 * @param number (int) The value of the integer.
	 * @throws MyNotOddNumberException
	 */
	public static void test(String name, int number) throws MyNotOddNumberException {
		if(number%2 == 0)
			throw new MyNotOddNumberException(name, number);
	}
}
