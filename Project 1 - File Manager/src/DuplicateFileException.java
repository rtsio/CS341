package proj1;

/**
 * Signals that another file already exists inside a <code>FileManager</code>.
 */
public class DuplicateFileException extends Exception {

	/**
	 * Constructs a DuplicateFileException with the default error detail message.
	 */
	public DuplicateFileException() {
		super("Error: such a file already exists.");
	}

	/**
	 * Constructs a DuplicateFileException with the specified error detail message.
	 */
	public DuplicateFileException(String message) {
		super(message);
	}
}