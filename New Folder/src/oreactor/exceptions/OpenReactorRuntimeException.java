package oreactor.exceptions;

public class OpenReactorRuntimeException extends RuntimeException {

	/**
	 * A serial version UID.
	 */
	private static final long serialVersionUID = 1366559662828470386L;

	public OpenReactorRuntimeException(String msg, Throwable t) {
		super(msg, t);
	}

	public OpenReactorRuntimeException(String msg) {
		this(msg, null);
	}
}
