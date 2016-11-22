package database;

import user.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * This class gathers information respective to Users, handles queries from the administrators 
 * regarding matching a user email to a user object.
 * 
 * @author William Granados, Kareem Mohamed, Harshdeep Grewal 
 *
 */
public class UserDatabase {

  /** Container for all of the current flights in our database.*/
  private static ArrayList<User> users = new ArrayList<User>();
  /** Current absolute path of saved file we're reading and writing to.*/
  private static String savedFilepath;

  /**
   * Loads all of the User information from information at file path.
   * 
   * @param filepath absolute path to the file containing information related to Users.
   */
  public UserDatabase(String filepath) {
    UserDatabase.savedFilepath = filepath;
    UserDatabase.readUsersFromFile(filepath);
  }


  /**
   * Loads all the information from the respective file into the database
   * Information read from file will be in the following format:
   * Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price 
   * An example is as follows: 
   * Roe;Richard;richard@email.com;21 First Lane Way;9999888877776666;2017-10-01
   * 
   * @param filepath File containing information on all of the flights.
   */
  public static void readUsersFromFile(String filepath) {
    File usersInformation = new File(filepath);
    users = new ArrayList<User>();
    if (savedFilepath == null) {
      savedFilepath = usersInformation.getAbsolutePath();
    }
    try {
      Scanner sc = new Scanner(usersInformation);
      while (sc.hasNextLine()) {
        String rawInformation = sc.nextLine();
        String[] entries = rawInformation.split(";");
        String lastName = entries[FileConstants.LAST_NAME];
        String firstName = entries[FileConstants.FIRST_NAME];
        String email = entries[FileConstants.EMAIL];
        String billingAddress = entries[FileConstants.BILLING_ADDRESS];
        String creditCardNumber = entries[FileConstants.CREDIT_CARD_NUMBER];
        Date creditCardExpirary = retrieveDateTime(entries[FileConstants.CREDIT_CARD_EXPIRARY]);
        User user = new User(email, billingAddress, firstName, lastName, creditCardNumber,
            creditCardExpirary);
        UserDatabase.addUser(user);
      }
      sc.close();
    } catch (FileNotFoundException exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Retrieves the date and creates an instance of Date using the non-depricated method.
   * 
   * @param date string in the form "YYYY-MM-DD"
   * @param time string in the form "HH:MM"
   * @return date instance
   */
  private static Date retrieveDateTime(String date) {
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
   * Get a array list of all the users.
   * 
   * @return the users
   */
  public static ArrayList<User> getUsers() {
    return users;
  }

  /**
   * Update the .csv file in accordance to the this users list.
   * 
   * @param client the client that will have their information changed
   * @param lastName the new lastname
   * @param firstName the new firstname
   * @param billingAddress the new billingaddress
   */
  public static void updateClientsPersonalInformation(User client, String lastName,
      String firstName, String billingAddress) throws IOException {
    List<String> newLines = new ArrayList<>();
    for (String line : Files.readAllLines(Paths.get(getSavedFilepath()), StandardCharsets.UTF_8)) {
      if (line.contains(client.getEmail())) {
        newLines.add(line.replace(client.getBillingAddress(), billingAddress));
        newLines.add(line.replace(client.getLastName(), lastName));
        newLines.add(line.replace(client.getFirstName(), firstName));
      }
      Files.write(Paths.get(getSavedFilepath()), newLines, StandardCharsets.UTF_8);

    }
  }

  /**
   * Switches the email of another user.
   * 
   * @param client the client who's email will be changed
   * @param email the new email
   */
  public static void updateClientsEmail(User client, String email) throws IOException {
    List<String> newLines = new ArrayList<>();
    for (String line : Files.readAllLines(Paths.get(getSavedFilepath()), StandardCharsets.UTF_8)) {
      if (line.contains(client.getEmail())) {
        newLines.add(line.replace(client.getEmail(), email));
      }
      Files.write(Paths.get(getSavedFilepath()), newLines, StandardCharsets.UTF_8);
    }
  }

  /**
   * Writes a user to the file path specified in {@link savedFilePath}; note that they are appended 
   * to the end of the file.
   * 
   * @param user the user that will be added.
   */
  public static void writeUserToFile(User user) {
    String email = user.getEmail();
    String billingAddress = user.getBillingAddress();
    String firstName = user.getFirstName();
    String lastName = user.getLastName();
    String creditcardNumber = user.getCreditCardNumber();
    Date creditCardExpiry = user.getCreditCardExpirary();
    
    DateFormat converter = new SimpleDateFormat("yyy-MM-dd");
    String creditCardExp = converter.format(creditCardExpiry);

    List<String> newLines = new ArrayList<>();

    newLines.add(email + ";" + billingAddress + ";" + firstName + ";" + lastName + ";"
        + creditcardNumber + "," + creditCardExp);
    try {
      Files.write(Paths.get(getSavedFilepath()), newLines, StandardOpenOption.APPEND);
    } catch (IOException exception) {
      exception.printStackTrace();
    }

  }

  /**
   * Adds a user object to our current database.
   * @param user user
   */
  public static void addUser(User user) {
    users.add(user);
  }


  /**
   * Returns the file path.
   * 
   * @return the saved file path
   */
  public static String getSavedFilepath() {
    return savedFilepath;
  }


  /**
   * Returns the line the database that corresponds to the User that has the Unique email email.
   * 
   * @param email the identifier
   * @return The line in the database that corresponds to the identifier, returns null otherwise
   */
  public static String getClientFromEmail(String email) {
    for (User client : users) {
      String clientemail = client.getEmail();
      if (clientemail.equals(email)) {
        return client.toString();
      }
    }
    return null;
  }

}

