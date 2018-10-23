package Database;

public class MyDBException extends RuntimeException {
	public MyDBException(Exception e) {
		super(e);
	}
}
