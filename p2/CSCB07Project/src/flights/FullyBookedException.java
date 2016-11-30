/**
 * 
 */
package flights;

/**
 * An exception class meant to be thrown when a client attempts
 * to book a trip on a flight with no seats left
 * @author Kareem
 */
public class FullyBookedException extends Exception {
	
	private static final long serialVersionUID = -8876279745060039342L;

	public FullyBookedException() {}
	
	public FullyBookedException(String message) { 
		super(message);
	}

}
