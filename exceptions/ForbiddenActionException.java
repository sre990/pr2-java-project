package exceptions;

public class ForbiddenActionException extends Exception{

	private static final long serialVersionUID = -2585335390843267535L;
	
	public ForbiddenActionException() {
		super();
	}
	
	public ForbiddenActionException(String msg) {
		super(msg);
	}
}