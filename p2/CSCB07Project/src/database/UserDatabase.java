package database;

import user.Client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class gathers information respective to Users, handles queries from the administrators
 * regarding matching a user email to a user object.
 * 
 * <p>Note that the singleton design pattern is applied to this class, so there will only ever
 * be one instance of this called accessible at all times. Meaning that modifications should be
 * made using the UserDatabase.getInstance()}.
 * 
 * @author William Granados, Kareem Mohamed, Harshdeep Grewal
 *
 */
public class UserDatabase {

  /** We'll be applying the singleton design pattern to our UserDatabase. */
  private static UserDatabase instance = new UserDatabase();
  /** Container for all of the current flights in our database. */
  private Map<String, Client> users;
  /** Current absolute path of saved file we're reading and writing to. */
  private String savedFilepath;

  private UserDatabase() {}

  /**
   * This method is applied since this is a single design pattern and should be accessed in a static
   * way.
   * 
   * @return an instance of this userDatabase
   */
  public static UserDatabase getInstance() {
    return instance;
  }

  /**
   * Loads all the information from the respective file into the database Information read from file
   * will be in the following format:
   * Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price An example is as
   * follows: Roe;Richard;richard@email.com;21 First Lane Way;9999888877776666;2017-10-01
   * 
   * <p>Note that all of the current information held in the instance will be erased, so call this
   * method sparingly.
   * 
   * @param filepath File containing information on all of the flights.
   */
  public void readUsersFromFile(String filepath) throws IOException {
    this.users = new HashMap<String, Client>();
    this.savedFilepath = filepath;
    BufferedReader br = new BufferedReader(new FileReader(this.savedFilepath));
    while (br.ready()) {
      String rawInformation = br.readLine().trim();
      String[] entries = rawInformation.split(";");
      String lastName = entries[FileConstants.LAST_NAME];
      String firstName = entries[FileConstants.FIRST_NAME];
      String email = entries[FileConstants.EMAIL];
      String billingAddress = entries[FileConstants.BILLING_ADDRESS];
      String creditCardNumber = entries[FileConstants.CREDIT_CARD_NUMBER];
      Date creditCardExpirary = retrieveDateTime(entries[FileConstants.CREDIT_CARD_EXPIRARY]);
      Client user = new Client(lastName, firstName, email, billingAddress, creditCardNumber,
          creditCardExpirary);
      // we attempt to add a new user to our database,
      // if there's a stacktrace print then we know that
      // there are multiple users with the same email.
      try {
        this.addUser(user);
      } catch (MultipleUsersException ex) {
        ex.printStackTrace();
      }
    }
    br.close();
  }

  /**
   * Retrieves the date and creates an instance of Date using the non-deprecated method.
   * 
   * @param date string in the form "YYYY-MM-DD"
   * @param time string in the form "HH:MM"
   * @return date instance
   */
  private Date retrieveDateTime(String date) {
    String[] dateEntries = date.split("-");
    // Based on the Calendar API we must change our months to be from 1-based to 0-based,
    // hence the -1 for the last variable.
    Integer[] dateVals = {Integer.parseInt(dateEntries[0]), Integer.parseInt(dateEntries[1]) - 1,
        Integer.parseInt(dateEntries[2])};
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, dateVals[0]);
    cal.set(Calendar.MONTH, dateVals[1]);
    cal.set(Calendar.DATE, dateVals[2]);
    return cal.getTime();
  }

  /**
   * Returns a map containing which associates the email of the user, to their respective User
   * object.
   * 
   * @return the users
   */
  public Map<String, Client> getUsers() {
    return users;
  }

  /**
   * Updates the user object which is currently contained in our database.
   *
   * <p>Note that these changes aren't saved until the method writeUserDatabaseToFile() is called;
   * which would typically be ran when the session is closed.
   * 
   * @param client client object 
   * @param lastName the new last name of the user
   * @param firstName the new first name of the user
   * @param billingAddress the new billing address of the user
   * 
   */
  public void updateClientsPersonalInformation(Client client, String lastName,
      String firstName, String billingAddress) {
    if (this.users.containsKey(client.getEmail())) {
      this.users.get(client.getEmail()).setLastName(lastName);
      this.users.get(client.getEmail()).setFirstName(firstName);
      this.users.get(client.getEmail()).setBillingAddress(billingAddress);
    }
  }

  /**
   * Updates the email to client relationship in our database.
   * 
   * <p>Note that this operation should only be done by an admin.
   * 
   * @param client the client who's email will be changed
   * @param email the new email
   */
  public void updateClientEmail(Client client, String email) {
    if (this.users.containsKey(client.getEmail())) {
      this.users.remove(client.getEmail());
      client.setEmail(email);
      this.users.put(email, client);
    }
  }

  /**
   * Writes the current database to file.
   * 
   * <p>Note that this command should be run sparingly as IO operations may be slow.
   * 
   * @throws IOException the file was deleted or something
   */
  public void writeUserDatabaseToFile() throws IOException {
    PrintWriter pw = new PrintWriter(new FileWriter(this.savedFilepath));
    for (Client user : this.getUsers().values()) {
      pw.println(user.toString());
    }
    pw.close();
  }


  /**
   * Adds a new user object to our current database; An error will be thrown if this user already
   * exists in our database.
   * 
   * @param user new user to be added to our database.
   * @throws MultipleUsersException this is thrown when there already exists a user with this email
   *         in our database.
   */
  public void addUser(Client user) throws MultipleUsersException {
    if (!this.users.containsKey(user.getEmail())) {
      this.users.put(user.getEmail(), user);
    } else {
      throw new MultipleUsersException(user.getEmail());
    }
  }

  /**
   * Returns the absolute path to the file containing our user information.
   * 
   * @return the saved file path
   */
  public String getSavedFilepath() {
    return savedFilepath;
  }


  /**
   * Returns the the raw database representation of this user from our text file.
   * 
   * @param email email that will be associated with the user
   * @return The line in the database that corresponds to the identifier, returns null otherwise
   */
  public String getClientFromEmail(String email) {
    for (Client client : this.getUsers().values()) {
      String clientEmail = client.getEmail();
      if (clientEmail.equals(email)) {
        return client.toString();
      }
    }
    return null;
  }

}

