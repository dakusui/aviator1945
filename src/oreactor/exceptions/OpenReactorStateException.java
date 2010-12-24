package oreactor.exceptions;


public class OpenReactorStateException extends OpenReactorRuntimeException {
	public OpenReactorStateException(String msg) {
		this(msg, null);
	}

	public OpenReactorStateException(String msg, Throwable t) {
		super(msg, t);
	}

	/**
	 *  A serial version UID.
	 */
	private static final long serialVersionUID = -5980949874741371897L;

}
