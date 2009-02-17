package avis.base;

public class AException extends Exception {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -3293151504232342848L;

	public AException() {
		super();
	}

	public AException(String message, Throwable cause) {
		super(message, cause);
	}

	public AException(String message) {
		super(message);
	}

	public AException(Throwable cause) {
		super(cause);
	}


}
