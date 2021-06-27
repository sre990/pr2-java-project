package exceptions;

public class TextFormatException extends Exception{

	private static final long serialVersionUID = -4848441532356701980L;
	
	public TextFormatException() {
		super();
	}
	
	public TextFormatException(String msg) {
		super(msg);
	}
}