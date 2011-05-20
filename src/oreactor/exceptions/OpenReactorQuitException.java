package oreactor.exceptions;


public class OpenReactorQuitException extends OpenReactorException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3881990082009197083L;
	
	public OpenReactorQuitException(String message, Throwable cause) {
		super(message, cause);
	}

	public OpenReactorQuitException(String message) {
		super(message);
	}
}
