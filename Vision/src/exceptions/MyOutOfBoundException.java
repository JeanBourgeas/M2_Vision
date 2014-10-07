package exceptions;

public class MyOutOfBoundException extends MyExceptions {
	private static final long serialVersionUID = 1L;

	public MyOutOfBoundException() {
		System.err.println("The number is out of bounds.");
	}
	
	public MyOutOfBoundException(String name) {
		System.err.println(name + " is out of bounds.");
	}
	
	public MyOutOfBoundException(int ofbNumber) {
		System.err.println("The number " + ofbNumber+" is out of bounds.");
	}

	public MyOutOfBoundException(int ofbNumber, int lowerLimit, int higherLimit) {
		System.err.println("The number " + ofbNumber + " is out of bounds. It should be between " + lowerLimit + " and " + higherLimit + ".");
	}

	public MyOutOfBoundException(String name, int ofbNumber, int lowerLimit, int higherLimit) {
		System.err.println(name + " = " + ofbNumber + " is out of bounds. It should be between " + lowerLimit + " and " + higherLimit + ".");
	}
	
	public static void test(String name, int number, int lowerLimit, int higherLimit) throws MyOutOfBoundException {
		if(number < lowerLimit || number > higherLimit)
			throw new MyOutOfBoundException(name, number, lowerLimit, higherLimit);
	}
}
