package us.thinkable.xcore.mmap;

public class MMException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MMException(String msg) {
		super(msg);
	}

	public MMException(String msg, Exception cause) {
		super(msg, cause);
	}

	public MMException(Exception cause) {
		super(cause);
	}
}
