package database;

/**
 * Constants which will be used to clarify parsing of information
 * from text files. 
 * 
 * @author william
 * @see FlightDatabase
 * @see UserDatabase
 */
public class FileConstants {

  /** Path to the csv file where flight information is stored.*/
  public static final String DEFAULT_FLIGHT_PATH = 
      "/home/wiliam/granad16/team_0600/p2/CSCB07Project/src/sampletests/flights1.txt";
  /** Path to the default users file.*/
  public static final String DEFAULT_USER_PATH = 
      "/home/wiliam/granad16/team_0600/p2/CSCB07Project/src/sampletests/clients.txt";
  /** Index of the flight number in a flight entry.*/
  public static final int FLIGHT_NUMBER = 0;
  /** Index of the departure date time in a flight entry.*/
  public static final int DEPARTURE_DATE_TIME = 1;
  /** Index of the arrival date time in a flight entry.*/
  public static final int ARRIVAL_DATE_TIME = 2;
  /** Index of the airline in a flight entry.*/
  public static final int AIRLINE = 3;
  /** Index of the origin in a flight entry.*/
  public static final int ORIGIN = 4;
  /** Index of the destination in a flight entry.*/
  public static final int DESTINATION = 5;
  /** Index of the price in a flight entry.*/
  public static final int COST = 6;
  //New from phase 3
  /** Index of the number of seats.*/
  public static final int NUM_SEATS = 7;
  
  /** Index of the last name in a user entry.*/
  public static final int LAST_NAME = 0;
  /** Index of the first name in a user entry.*/
  public static final int FIRST_NAME = 1;
  /** Index of the email in a user entry.*/
  public static final int EMAIL = 2;
  /** Index of the billing address in a user entry.*/
  public static final int BILLING_ADDRESS = 3;
  /** Index of the credit card number in a user entry.*/
  public static final int CREDIT_CARD_NUMBER = 4;
  /** Index of the credit card expirary date in a user entry.*/
  public static final int CREDIT_CARD_EXPIRARY = 5;

}
