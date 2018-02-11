package us.thinkable.xcore;
/**
 * CoreException is used to throw arbitrary exceptions from the Core library.
 * 
 * @author Gregory Smith
 *
 */
public class CoreException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CoreException() {
		super();
	}

	public CoreException(String msg) {
		super(msg);
	}

	public CoreException(Exception ex) {
		super(ex);
	}

	public CoreException(String msg, Exception ex) {
		super(msg, ex);
	}
}
