package proj1;

/**
 * Signals that there are not enough bytes inside a <code>File</code> to truncate by a given amount.
 */
public class FileUnderflowException extends Exception {

	/**
	 * Constructs a FileUnderflowException with the default error detail message.
	 */
	public FileUnderflowException() {
		super("Error: the file cannot be truncated by more than it's actual size.");
	}

	/**
	 * Constructs a FileUnderflowException with the specified error detail message.
	 */
	public FileUnderflowException(String message) {
		super(message);
	}

}
