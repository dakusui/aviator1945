package oreactor.exceptions;

public class OpenReactorExitException extends OpenReactorException {

	/**
	 * A serial version UID
	 */
	private static final long serialVersionUID = 7654353905624233010L;

	public OpenReactorExitException(String msg, Throwable t) {
		super(msg, t);
	}


	public OpenReactorExitException(String msg) {
		super(msg);
	}

}
