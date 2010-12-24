package oreactor.exceptions;

public class OpenReactorException extends Exception {
	/**
	 * A serial version UID
	 */
	private static final long serialVersionUID = -4654232703191773582L;

	public OpenReactorException(String msg, Throwable t) {
		super(msg, t);
	}

	public OpenReactorException(String msg) {
		this(msg, null);
	}

}
