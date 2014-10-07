package exceptions;

public class MyNotPositiveNumberException extends MyExceptions {
	private static final long serialVersionUID = 1L;
	
	public MyNotPositiveNumberException(int number) {
		System.err.println("The number " + number + " is not positive.");
	}

	public MyNotPositiveNumberException(String name, int number) {
		System.err.println(name + " = " + number + " is not positive.");
	}
	
	public static void test(String name, int number) throws MyNotPositiveNumberException {
		if(number < 0)
			throw new MyNotPositiveNumberException(name, number);
	}
}
