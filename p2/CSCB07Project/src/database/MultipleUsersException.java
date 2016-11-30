package database;


/**
 * This is run when we attempt to add users with the same identifier to our database.
 * @author wiliam
 */
public class MultipleUsersException extends Exception {

  /**
   * default serial id .
   */
  private static final long serialVersionUID = 1L;

  public MultipleUsersException(String msg) {
    super(msg);
  }

}
