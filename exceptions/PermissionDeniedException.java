package exceptions;

public class PermissionDeniedException extends Exception{
	
	private static final long serialVersionUID = 2029165747297489706L;
	
	public PermissionDeniedException() {
		super();
	}
	
	public PermissionDeniedException(String msg) {
		super(msg);
	}

}