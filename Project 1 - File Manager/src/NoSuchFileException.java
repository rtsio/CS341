package proj1;

/**
 * Signals that the <code>File</code> requested does not exist inside a <code>FileManager</code>.
 */
public class NoSuchFileException extends Exception {

	/**
	 * Constructs a NoSuchFileException with the default error detail message.
	 */
	public NoSuchFileException() {
		super("Error: no such file exists.");
	}
	
	/**
	 * Constructs a NoSuchFileException with the specified error detail message.
	 */
	public NoSuchFileException(String message) {
		super(message);
	}

}
