package database;

import flights.Flight;
import flights.Itinerary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


/**
 * This class gathers information respective to flights, handles queries from the users regarding
 * the way to get from from destination to another, and handles adding and updating existing flights
 * in our system.
 * 
 * @author William Granados, Kareem Mohamed(THE LEGEND), Harshdeep Grewal, Lian Pakingan 
 *
 */
public class FlightDatabase {

  /** Container for all of the current flights in our database. */
  private static ArrayList<Flight> flights = new ArrayList<Flight>();
  /** Graph representation of our cities. */
  private static Map<String, ArrayList<Flight>> adjacencyList;
  /** Current absolute path of saved file we're reading and writing to.*/
  private static String savedFilepath;

  /**
   * Loads all of the flight information from file specified at file path.
   * 
   * @param filepath absolute path to the file containing information related to flights.
   */
  public FlightDatabase(String filepath) {
    FlightDatabase.savedFilepath = filepath;
    FlightDatabase.flights = new ArrayList<Flight>();
    FlightDatabase.readFlightsFromFile(filepath);
    this.generateGraph();
  }

  /**
   * Loads all of the flight information from {@link FileConstants.DEFAULT_FLIGHT_PATH} specified at
   * file path.
   * 
   */
  public FlightDatabase() {
    FlightDatabase.flights = new ArrayList<Flight>();
    FlightDatabase.readFlightsFromFile(FileConstants.DEFAULT_FLIGHT_PATH);
    this.generateGraph();
  }

  /**
   * Return the array list of flights.
   * 
   * @return the flights
   */
  public static ArrayList<Flight> getFlights() {
    return flights;
  }

  /**
   * Adds new flight to our database which will be used during queries.
   * 
   * @param flight new flight object
   */
  public static void addFlight(Flight flight) {
    FlightDatabase.flights.add(flight);
  }

  /**
   * Generates the implicit graph created from the list of Flights.
   * We generate an adjacency list where each city maps to a flight, this is needed for when we do
   * queries.
   */
  private void generateGraph() {
    FlightDatabase.adjacencyList = new HashMap<String, ArrayList<Flight>>();
    for (int i = 0; i < FlightDatabase.flights.size(); i++) {
      String origin = FlightDatabase.flights.get(i).getOrigin();
      if (!FlightDatabase.adjacencyList.containsKey(origin)) {
        FlightDatabase.adjacencyList.put(origin, new ArrayList<Flight>());
      }
      FlightDatabase.adjacencyList.get(origin).add(FlightDatabase.flights.get(i));
    }
  }

  /**
   * Loads all the information from the respective file into the database
   * Information read from file will be in the following format:
   * LastName;FirstNames;Email;Address;CreditCardNumber;ExpiryDate
   * An example is as follows: 
   * 
   * @param filepath File containing information on all of the flights.
   */
  public static void readFlightsFromFile(String filepath) {
    File flightInformation = new File(filepath);
    try {
      Scanner sc = new Scanner(flightInformation);
      while (sc.hasNextLine()) {
        String rawInformation = sc.nextLine();
        String[] entries = rawInformation.split(";");
        String flightNumber = entries[FileConstants.FLIGHT_NUMBER];
        // Note that arrays returned from retrieveDateTime have 2 entries, the first
        // being a date "YYYY-MM-DD" and a time "HH:MM"
        String[] departDateTimeEntries =
            retrieveDateTimeEntries(entries[FileConstants.DEPARTURE_DATE_TIME]);
        Date departDateTime = retrieveDateTime(departDateTimeEntries[0], 
            departDateTimeEntries[1]);
        String[] arrivalDateTimeEntries =
            retrieveDateTimeEntries(entries[FileConstants.ARRIVAL_DATE_TIME]);
        Date arrivalDateTime =
            retrieveDateTime(arrivalDateTimeEntries[0], arrivalDateTimeEntries[1]);
        String airline = entries[FileConstants.AIRLINE];
        String origin = entries[FileConstants.ORIGIN];
        String destination = entries[FileConstants.DESTINATION];
        Double cost = Double.parseDouble(entries[FileConstants.COST]);
        Flight flight = new Flight(flightNumber, departDateTime, arrivalDateTime, airline, origin,
            destination, cost);
        FlightDatabase.addFlight(flight);
      }
      sc.close();
    } catch (FileNotFoundException exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Splits a string of the format "2016-09-09 09:09" by the whitespace.
   * 
   * @param unformattedDateTime string in the format "2016-09-09 09:09"
   * @return string array with entries for date and time for the flight
   */
  private static String[] retrieveDateTimeEntries(String unformattedDateTime) {
    String[] dateTimeInfo = unformattedDateTime.split(" ");
    return dateTimeInfo;
  }

  /**
   * Retrieves the date and creates an instance of Date using the non-depricated method.
   * 
   * @param date string in the form "YYYY-MM-DD"
   * @param time string in the form "HH:MM"
   * @return date instance
   */
  private static Date retrieveDateTime(String date, String time) {
    String[] dateEntries = date.split("-");
    String[] timeEntries = time.split(":");
    // Based on the Calendar API we must change our months to be from 1-based to 0-based,
    // hence the -1 for the month.
    Integer[] dateVals = {Integer.parseInt(dateEntries[0]), Integer.parseInt(dateEntries[1]) - 1,
        Integer.parseInt(dateEntries[2])};
    Integer[] timeVals = {Integer.parseInt(timeEntries[0]), Integer.parseInt(timeEntries[1])};
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, dateVals[0]);
    cal.set(Calendar.MONTH, dateVals[1]);
    cal.set(Calendar.DAY_OF_MONTH, dateVals[2]);
    cal.set(Calendar.HOUR, timeVals[0]);
    cal.set(Calendar.MINUTE, timeVals[1]);
    return cal.getTime(); 
  }

  /**
   * Generates all Itineraries, not sorted.
   * @param departureDate Date
   * @param origin starting point
   * @param destination ending point
   * @return the array list of all possible Itineraries
   */
  public static ArrayList<Itinerary> generateAllItineraries(String departureDate, 
      String origin, String destination) {
    Queue<Itinerary> queue = new LinkedList<Itinerary>();
    // To being we will push all outgoing flights from the starting city to our search
    for (int i = 0; i < FlightDatabase.flights.size(); i++) {
      if (FlightDatabase.isFlightStartingPoint(FlightDatabase.flights.get(i), 
          departureDate, origin)) {
        ArrayList<Flight> startingPointInit = new ArrayList<Flight>();
        startingPointInit.add(FlightDatabase.flights.get(i));
        Itinerary startingPoint = new Itinerary(startingPointInit);
        queue.add(startingPoint);
      }
    }
    Set<Itinerary> visited = new TreeSet<Itinerary>();
    ArrayList<Itinerary> ret = new ArrayList<Itinerary>();
    // Now we will start branching out from out destinations,
    // adding in possible Itineraries on the way
    while (!queue.isEmpty()) {
      Itinerary currentItinerary = queue.poll();
      String city = currentItinerary.getDestination();
      if (city.equals(destination)) {
        ret.add(currentItinerary);
        continue;
      }
      // this is the propagation stage where we'll be going to other nodes in our graph
      for (Flight outGoingFlight : FlightDatabase.adjacencyList.get(city)) {
        Itinerary newItinerary = currentItinerary;
        newItinerary.addFlight(outGoingFlight);
        if (!visited.contains(newItinerary)) {
          visited.add(newItinerary);
          queue.add(newItinerary);
        }
      } 
    }
    return ret;
  }


  /**
   * Generates a list of Itinerary which includes all possible ways to go from one city being the
   * origin, to the ending city being the destination; the result is sorted by total cost.
   * This method creates an implicit graph from the Flights in our database and generates the
   * Itineraries by doing a breadth first search.
   * 
   * @param departureDate object specifying the time in which we will depart from
   * @param origin object specifying the starting city we are departing from
   * @param destination object specifying the ending city we are traveling to
   * @return list of Itinerary which we will use
   */
  public static ArrayList<Itinerary> queryIteneraryByCost(String departureDate, String origin,
      String destination) {
    ArrayList<Itinerary> arrayListItineraries = 
        FlightDatabase.generateAllItineraries(departureDate, origin, destination);
    Collections.sort(arrayListItineraries, (f1, f2) -> f1.getTotalCost()
        < f2.getTotalCost() ? -1 : 
            (f1.getTotalCost() == f2.getTotalCost() ? 0 : 1));
    return arrayListItineraries;
  }


  /**
   * Generates a list of Itinerary which includes all possible ways to go from one city being the
   * origin, to the ending city being the destination; the result is sorted by total time taken.
   * This method creates an implicit graph from the Flights in our database and generates the
   * Itineraries by doing a breadth first search.
   * 
   * @param departureDate object specifying the time in which we will depart from
   * @param origin object specifying the starting city we are departing from
   * @param destination object specifying the ending city we are traveling to
   * @return list of Itinerary which we will use
   */
  public static ArrayList<Itinerary> queryIteneraryByTime(String departureDate, String origin,
      String destination) {
    ArrayList<Itinerary> arrayListItineraries = 
        FlightDatabase.generateAllItineraries(departureDate, origin, destination);
    Collections.sort(arrayListItineraries, (f1, f2) -> f1.getTotalTravelTime().getMinutes() 
        < f2.getTotalTravelTime().getMinutes() ? -1 : 
            (f1.getTotalTravelTime().getMinutes() == f2.getTotalTravelTime().getMinutes() ? 0 : 1));
    return arrayListItineraries;
  }

  /**
   * get the filepath.
   * @return the savedFilepath
   */
  public static String getSavedFilepath() {
    return savedFilepath;
  }

  private static boolean isFlightStartingPoint(Flight flight, 
        String departureDate, String origin) {
    return flight.getOrigin().equals(origin)
        && flight.getDepartureDateTime().equals(departureDate);
  }
  
  /**
   * @param flight the flight that is meant to be added.
   */
  public static void writeFlightToFile(Flight flight) { 
    String flightNumber = flight.getFlightNumber();
    Date departureDateTime = flight.getDepartureDateTime();
    Date arrivalDateTime = flight.getArrivalDateTime();
    String airline = flight.getAirline();
    String origin = flight.getOrigin();
    String destination = flight.getDestination();

    List<String> newLines = new ArrayList<>();

    newLines.add(flightNumber + ";" + departureDateTime.toString() 
        + ";" + arrivalDateTime.toString() + ";" + airline + ";" + origin + ";" + destination);
    try {
      Files.write(Paths.get(savedFilepath), newLines, StandardOpenOption.APPEND);
    } catch (IOException exception) {
      // TODO Auto-generated catch block
      exception.printStackTrace();
    }
  }
  
  /**
   * Returns a list of flights that go from origin to destination directly on a certain date.
   * @param date given
   * @param origin starting point
   * @param destination ending point
   * @return List of lines in the Flight databases that satisfies these queries
   * @throws IOException exception
   */
  public static List<String> getDirectFlights(String date, 
      String origin, String destination) throws IOException { 
    List<String> ret = new ArrayList<String>();
    for (Flight flight : flights) {
      DateFormat converter = new SimpleDateFormat("yyyy-MM-dd");
      String flightDay = converter.format(flight.getDepartureDateTime());
      if (flightDay.equals(date)) {
        if (flight.getOrigin().equals(origin)) {
          if (flight.getDestination().equals(destination)) {
            ret.add(flight.toString());
          }
        }
      }
    }
    return ret;
  }

}

