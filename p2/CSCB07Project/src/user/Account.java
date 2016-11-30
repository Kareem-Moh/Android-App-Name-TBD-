package user;

/**
 * A class to represent a User's account.
 * 
 * @author Kareem Mohamed
 * @author Harsh Grewal
 */
public abstract class Account {

  protected String email;
  private String firstName;
  private String lastName;

  /**
   * Constructor.
   * @param email the email
   * @param firstName the first name
   * @param lastName the last name
   */
  public Account(String lastName, String firstName, String email) {
    super();
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }


  /**
   * get first name.
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }


  /**
   * set first name.
   * 
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * get last name.
   * 
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * set last name.
   * 
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


  /**
   * get email.
   * 
   * @return the email
   */
  public String getEmail() {
    return email;
  }
  
  /**
   * note that this should only be accessed by an admin.
   * @param email new email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Update all the information you're allowed to update all at once.
   * 
   * @param billingAddress.
   * @param firstName.
   * @param lastName.
   */
  public void updateInformation(String firstName, String lastName) {
    this.setFirstName(firstName);
    this.setLastName(lastName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    return result;
  }
}
