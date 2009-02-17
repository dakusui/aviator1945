package avis.motion;

import avis.base.ARuntimeException;

public class MMachineStateException extends ARuntimeException {
	/**
	 * Serial version Id
	 */
	private static final long serialVersionUID = 1L;

	public MMachineStateException() {
		super();
	}

	public MMachineStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public MMachineStateException(String message) {
		super(message);
	}

	public MMachineStateException(Throwable cause) {
		super(cause);
	}

}
