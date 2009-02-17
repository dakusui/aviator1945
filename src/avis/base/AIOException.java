package avis.base;

public class AIOException extends AException {
	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 7703002512064522611L;


	public AIOException() {
		super();
	}

	public AIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public AIOException(String message) {
		super(message);
	}

	public AIOException(Throwable cause) {
		super(cause);
	}

}
