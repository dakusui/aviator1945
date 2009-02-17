package avis.spec;

import avis.base.AException;

public class ASpecException extends AException {
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 1L;


	public ASpecException() {
		super();
	}

	public ASpecException(String message, Throwable cause) {
		super(message, cause);
	}

	public ASpecException(String message) {
		super(message);
	}

	public ASpecException(Throwable cause) {
		super(cause);
	}

}
