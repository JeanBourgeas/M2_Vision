package exceptions;

public class MyNotOddNumberException extends MyExceptions {
	private static final long serialVersionUID = 1L;

	public MyNotOddNumberException() {
		System.err.println("The number is not an odd number.");
	}
	
	public MyNotOddNumberException(String name) {
		System.err.println(name + " is not an odd number");
	}
	
	public MyNotOddNumberException(int number) {
		System.err.println("The number " + number + " is not an odd number.");
	}

	public MyNotOddNumberException(String name, int number) {
		System.err.println(name + " = " + number + " is not an odd number.");
	}
	
	public static void test(String name, int number) throws MyNotOddNumberException {
		if(number%2 == 0)
			throw new MyNotOddNumberException(name, number);
	}
}
