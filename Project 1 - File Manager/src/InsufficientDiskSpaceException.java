package proj1;

/**
 * Signals that there is not enough space to create or extend a <code>File</code> inside a <code>FileManager</code>.
 */
public class InsufficientDiskSpaceException extends Exception {

	/**
	 * Constructs an InsufficientDiskSpaceException with the default error detail message.
	 */
	public InsufficientDiskSpaceException() {
		super("Error: insufficient disk space.");
	}

	/**
	 * Constructs an InsufficientDiskSpaceException with the specified error detail message.
	 */
	public InsufficientDiskSpaceException(String message) {
		super(message);
	}



}
