package exceptions;

/**
 * Use this Exception when two integers should be equals but are not.
 * @author Jean
 *
 */
public class MyNotEqualNumberException extends MyExceptions {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Exception : </br>
	 * "The number " + number1 + " is not equal to the number " + number2 + "."
	 * @param number1 (int)	The value of the first integer.
	 * @param number2 (int) The value of the second integer.
	 */
	public MyNotEqualNumberException(int number1, int number2) {
		System.err.println("The number " + number1 + " is not equal to the number " + number2 + ".");
	}

	/**
	 * Exception : </br>
	 * name1 + " = " + number1 + " is not equal to " + name2 + " = " + number2 + "."
	 * @param name1 (String) The name of the first integer.
	 * @param number1 (int)	The value of the first integer.
	 * @param name2 (String) The name of the second integer.
	 * @param number2 (int) The value of the second integer.
	 */
	public MyNotEqualNumberException(String name1, int number1, String name2, int number2) {
		System.err.println(name1 + " = " + number1 + " is not equal to " + name2 + " = " + number2 + ".");
	}
	
	/**
	 * Use this to test if two integers are equals and throw the exception if needed.
	 * @param name1 (String) The name of the first integer.
	 * @param number1 (int)	The value of the first integer.
	 * @param name2 (String) The name of the second integer.
	 * @param number2 (int) The value of the second integer.
	 * @throws MyNotEqualNumberException
	 */
	public static void test(String name1, int number1, String name2, int number2) throws MyNotEqualNumberException {
		if(number1 != number2)
			throw new MyNotEqualNumberException(name1, number1, name2, number2);
	}
}
