package exceptions;

public class MyNotEqualNumberException extends MyExceptions {
	private static final long serialVersionUID = 1L;
	
	public MyNotEqualNumberException(int number1, int number2) {
		System.err.println("The number " + number1 + " is not equal to the number " + number2 + ".");
	}

	public MyNotEqualNumberException(String name1, int number1, String name2, int number2) {
		System.err.println(name1 + " = " + number1 + " is not equal to " + name2 + " = " + number2 + ".");
	}
	
	public static void test(String name1, int number1, String name2, int number2) throws MyNotEqualNumberException {
		if(number1 != number2)
			throw new MyNotEqualNumberException(name1, number1, name2, number2);
	}
}
