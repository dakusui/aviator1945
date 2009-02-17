package avis.base;

public class ARuntimeException extends RuntimeException {
	/**
	 * シリアル・バージョン
	 */
	private static final long serialVersionUID = -5188284182884596838L;

	public ARuntimeException() {
		super();
	}

	public ARuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ARuntimeException(String message) {
		super(message);
	}

	public ARuntimeException(Throwable cause) {
		super(cause);
	}

}
