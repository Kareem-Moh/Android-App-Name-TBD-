package driver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** A Driver used for autotesting the project backend. */
public class Driver {

  public static final Duration MIN_LAYOVER = Duration.ofMinutes(30);  
  public static final Duration MAX_LAYOVER = Duration.ofHours(6);  

  private static DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
  private static DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  /**
   * Uploads client information to the application from the file at the
   * given path.
   * @param path the path to an input csv file of client information with
   *     lines in the format: 
   *     LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
   *     The ExpiryDate is stored in the format yyyy-MM-dd.
   */
  public static void uploadClientInfo(String path) {
    // TODO: complete this method body
  }

  /**
   * Uploads flight information to the application from the file at the
   * given path.
   * @param path the path to an input csv file of flight information with 
   *     lines in the format: 
   *     Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,Price
   *     The dates are in the format yyyy-MM-dd HH:mm; the price has exactly two
   *     decimal places.
   */
  public static void uploadFlightInfo(String path) {
    // TODO: complete this method body
  }

  /**
   * Returns the information stored for the client with the given email. 
   * @param email the email address of a client
   * @return the information stored for the client with the given email
   *     in this format:
   *     LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
   *     (the ExpiryDate is stored in the format yyyy-MM-dd)
   */
  public static String getClient(String email) {

    // TODO: complete/rewrite this method body
    // The code below gives you the format in which the auto-tester expects the output.

    String lastName = "LastName";
    String firstNames = "FirstName MiddleName";
    String address = "Street, City, Country";
    String ccNumber = "12341231234";
    Date expiryDate = null;
    try {
      expiryDate = date.parse("2019-03-01");
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return String.format("%s;%s;%s;%s;%s;%s", 
        lastName, firstNames, email, address, ccNumber, date.format(expiryDate));
  }

  /**
   * Returns all flights that depart from origin and arrive at destination on
   * the given date. 
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight origin
   * @param destination a flight destination
   * @return the flights that depart from origin and arrive at destination
   *     on the given date formatted in exactly this format:
   *     Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,Price
   *     The dates are in the format yyyy-MM-dd HH:mm; the price has exactly two
   *     decimal places. 
   * @throws ParseException if date cannot be parsed
   */
  public static List<String> getFlights(String date, String origin, String destination) 
      throws ParseException {
    // TODO: complete/rewrite this method body
    // The code below gives you the format in which the auto-tester expects the output.

    String flightNum = "AC213";
    Date departure = dateTime.parse("2016-11-29 11:30");
    Date arrival = dateTime.parse("2016-11-29 15:45");
    String airline = "Air Canada";
    double price = 630.99;
    
    String oneFlightFormatted = String.format("%s;%s;%s;%s;%s;%s;%.2f", 
        flightNum, dateTime.format(departure), dateTime.format(arrival),
        airline, origin, destination, price);
    List<String> flights = new ArrayList<>();
    flights.add(oneFlightFormatted);
    
    return flights;
  }

  /**
   * Returns all itineraries that depart from origin and arrive at destination on the given date. If
   * an itinerary contains two consecutive flights F1 and F2, then the destination of F1 should
   * match the origin of F2. To simplify our task, if there are more than MAX_LAYOVER hours or less
   * than MIN_LAYOVER between the arrival of F1 and the departure of F2, then we do not consider
   * this sequence for a possible itinerary.
   * 
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries that depart from origin and arrive at destination on the given date with
   *         valid layover. Each itinerary in the output should contain one line per flight, in the
   *         format: 
   *         Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
   *         followed by total price (on its own line, exactly two decimal places), followed by
   *         total duration (on its own line, measured in hours with 2 decimal places).
   */
  public static List<String> getItineraries(String date, String origin, String destination) {
    // TODO: complete/rewrite this method body
    // The code below gives you the format in which the auto-tester expects the output,
    // as well as some ideas about the built-in Java classes to handle dates, times, etc.
    // Make sure to read the API carefully: what you need will depend on your design!

    String flightNum1 = "AC213";
    Date departure1 = null;
    Date arrival1 = null;
    try {
      departure1 = dateTime.parse("2016-11-29 11:30");
      arrival1 = dateTime.parse("2016-11-29 15:45");
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    String airline1 = "Air Canada";
    double price1 = 630.99;
        
    String flightNum2 = "AC768";
    Date departure2 = null;
    Date arrival2 = null;
    try {
      departure2 = dateTime.parse("2016-11-29 19:30");
      arrival2 = dateTime.parse("2016-11-29 22:00");
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    String airline2 = "Air Canada Rouge";
    double price2 = 530.99;
    
    String stopover = "YYZ";
    double totalPrice = price1 + price2;
    Duration totalDuration = Duration.between(departure1.toInstant(), arrival2.toInstant());

    String flight1Formatted = String.format("%s;%s;%s;%s;%s;%s", 
        flightNum1, dateTime.format(departure1), dateTime.format(arrival1),
        airline1, origin, stopover);
    String flight2Formatted = String.format("%s;%s;%s;%s;%s;%s", 
        flightNum2, dateTime.format(departure2), dateTime.format(arrival2),
        airline2, stopover, destination);
    String oneItineraryFormatted = String.format("%s\n%s\n%.2f\n%.2f",
        flight1Formatted, flight2Formatted, totalPrice, totalDuration.toMinutes() / 60.0);
    
    List<String> itineraries = new ArrayList<>();
    itineraries.add(oneItineraryFormatted);
    
    return itineraries;
  }

  /**
   * Returns the same itineraries as getItineraries produces, but sorted according to total
   * itinerary cost, in non-decreasing order.
   * 
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries (sorted in non-decreasing order of total itinerary cost) in the same format
   *         as in getItineraries.
   */
  public static String getItinerariesSortedByCost(String date, String origin, String destination) {
    // TODO: complete this method body
    return null;
  }

  /**
   * Returns the same itineraries as getItineraries produces, but sorted according
   * to total itinerary travel time, in non-decreasing order.
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries (sorted in non-decreasing order of total travel time) in the same format
   *         as in getItineraries.
   */
  public static String getItinerariesSortedByTime(String date, String origin, String destination) {
    // TODO: complete this method body
    return null;
  }
}