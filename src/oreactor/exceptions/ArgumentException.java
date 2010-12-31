package oreactor.exceptions;

/**
 * An exception class which notifies an illegal argument is given to an <code>ArgParser</code> objeect.
 * @author hiroshi
  */
public class ArgumentException extends OpenReactorException {
	/**
	 * A serial version UID. 
	 */
	private static final long serialVersionUID = -3576070361293973515L;

	/**
	 * Constructs a new exception object of this class.
	 * @param msg Message.
	 */
	ArgumentException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new exception object of this class.
	 * @param msg Message.
	 * @param t A nested Exception.
	 */
	ArgumentException(String msg, Throwable t) {
		super(msg,t);
	}

}
