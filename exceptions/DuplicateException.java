package exceptions;

public class DuplicateException extends Exception{
	
	private static final long serialVersionUID = 407678081262532273L;
	
	public DuplicateException() {
		super();
	}
	
	public DuplicateException(String msg) {
		super(msg);
	}
}