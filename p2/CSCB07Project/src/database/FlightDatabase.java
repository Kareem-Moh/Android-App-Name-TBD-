package database;

import flights.Flight;
import flights.Itinerary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


/**
 * This class gathers information respective to flights, handles queries from the users regarding
 * the way to get from from destination to another, and handles adding and updating existing flights
 * in our system.
 * 
 * @author William Granados, Kareem Mohamed(THE LEGEND), Harshdeep Grewal, Lian Pakingan
 *
 */
public class FlightDatabase {

  /** We'll be applying the singleton design pattern to our FlightDatabase. */
  private static FlightDatabase instance = new FlightDatabase();
  /** Container for all of the current flights in our database. */
  private ArrayList<Flight> flights = new ArrayList<Flight>();
  /** Graph representation of our cities. */
  private Map<String, ArrayList<Flight>> adjacencyList;
  /** Current absolute path of saved file we're reading and writing to. */
  private String savedFilepath;

  public static final Duration MIN_LAYOVER = Duration.ofMinutes(30);
  public static final Duration MAX_LAYOVER = Duration.ofHours(6);


  public FlightDatabase() {}

  public static FlightDatabase getInstance() {
    return instance;
  }


  /**
   * Return the array list of flights.
   * 
   * @return the flights
   */
  public ArrayList<Flight> getFlights() {
    return this.flights;
  }

  /**
   * Adds new flight to our database which will be used during queries.
   * 
   * @param flight new flight object
   */
  public void addFlight(Flight flight) {
    this.flights.add(flight);
  }

  /**
   * Generates the implicit graph created from the list of Flights. We generate an adjacency list
   * where each city maps to a flight, this is needed for when we do queries.
   */
  private void generateGraph() {
    this.adjacencyList = new HashMap<String, ArrayList<Flight>>();
    for (int i = 0; i < this.flights.size(); i++) {
      String origin = this.flights.get(i).getOrigin();
      if (!this.adjacencyList.containsKey(origin)) {
        this.adjacencyList.put(origin, new ArrayList<Flight>());
      }
      this.adjacencyList.get(origin).add(this.flights.get(i));
    }
  }

  /**
   * Loads all the information from the respective file into the database Information read from file
   * will be in the following format: LastName;FirstNames;Email;Address;CreditCardNumber;ExpiryDate
   * An example is as follows:
   * 
   * <p>Also generates the corresponding graph used for queries later on.
   * 
   * @param filepath File containing information on all of the flights.
   */
  public void readFlightsFromFile(String filepath) throws IOException {
    this.flights = new ArrayList<Flight>();
    this.savedFilepath = filepath;
    BufferedReader br = new BufferedReader(new FileReader(this.savedFilepath));
    while (br.ready()) {
      String rawInformation = br.readLine().trim();
      String[] entries = rawInformation.split(";");
      String flightNumber = entries[FileConstants.FLIGHT_NUMBER];
      // Note that arrays returned from retrieveDateTime have 2 entries, the first
      // being a date "YYYY-MM-DD" and a time "HH:MM"
      String[] departDateTimeEntries =
          retrieveDateTimeEntries(entries[FileConstants.DEPARTURE_DATE_TIME]);
      Date departDateTime = retrieveDateTime(departDateTimeEntries[0], departDateTimeEntries[1]);
      String[] arrivalDateTimeEntries =
          retrieveDateTimeEntries(entries[FileConstants.ARRIVAL_DATE_TIME]);
      Date arrivalDateTime = retrieveDateTime(arrivalDateTimeEntries[0], arrivalDateTimeEntries[1]);
      String airline = entries[FileConstants.AIRLINE];
      String origin = entries[FileConstants.ORIGIN];
      String destination = entries[FileConstants.DESTINATION];
      Double cost = Double.parseDouble(entries[FileConstants.COST]);
      int numSeats = Integer.parseInt(entries[FileConstants.NUM_SEATS]);
      Flight flight = new Flight(flightNumber, departDateTime, arrivalDateTime, airline, origin,
          destination, cost, numSeats);
      this.addFlight(flight);
    }
    this.generateGraph();
    br.close();
  }

  /**
   * Splits a string of the format "2016-09-09 09:09" by the whitespace.
   * 
   * @param unformattedDateTime string in the format "2016-09-09 09:09"
   * @return string array with entries for date and time for the flight
   */
  private String[] retrieveDateTimeEntries(String unformattedDateTime) {
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
  private Date retrieveDateTime(String date, String time) {
    final int timeZoneOffset = 12;
    String[] dateEntries = date.split("-");
    String[] timeEntries = time.split(":");
    // Based on the Calendar API we must change our months to be from 1-based to 0-based,
    // hence the -1 for the month.
    Integer[] dateVals = {Integer.parseInt(dateEntries[0]), Integer.parseInt(dateEntries[1]) - 1,
        Integer.parseInt(dateEntries[2])};
    // Due to some erranous errors it would seem that we have to deduct 12 hours from all of our
    // entries to be in line with the entries in our database.
    Integer[] timeVals =
        {Integer.parseInt(timeEntries[0]) - timeZoneOffset, Integer.parseInt(timeEntries[1])};
    Calendar cal = Calendar.getInstance();
    cal.clear(Calendar.ZONE_OFFSET);
    cal.clear(Calendar.DST_OFFSET);
    cal.set(Calendar.YEAR, dateVals[0]);
    cal.set(Calendar.MONTH, dateVals[1]);
    cal.set(Calendar.DAY_OF_MONTH, dateVals[2]);
    cal.set(Calendar.HOUR, timeVals[0]);
    cal.set(Calendar.MINUTE, timeVals[1]);
    return cal.getTime();
  }

  /**
   * Generates all Itineraries, not sorted.
   * 
   * @param departureDate Date
   * @param origin starting point
   * @param destination ending point
   * @return the array list of all possible Itineraries
   */
  public ArrayList<Itinerary> generateAllItineraries(String departureDate, String origin,
      String destination) {
    Queue<Itinerary> queue = new LinkedList<Itinerary>();
    // To being we will push all outgoing flights from the starting city to our search
    for (int i = 0; i < this.flights.size(); i++) {
      if (this.isFlightStartingPoint(this.flights.get(i), departureDate, origin)) {
        ArrayList<Flight> startingPointInit = new ArrayList<>(Arrays.asList(this.flights.get(i)));
        Itinerary startingPoint = new Itinerary(startingPointInit);
        queue.add(startingPoint);
      } 
    }
    Set<Itinerary> visited = new HashSet<Itinerary>();
    ArrayList<Itinerary> ret = new ArrayList<Itinerary>();
    // Now we will start branching out from out destinations,
    // adding in possible Itineraries on the way
    while (!queue.isEmpty()) {
      Itinerary currentItinerary = queue.poll();
      String city = currentItinerary.getDestination();
      //System.out.println(currentItinerary);
      if (city.equals(destination)) {
        ret.add(currentItinerary);
        continue;
      }
      Itinerary newItinerary = null;
      // this is the propagation stage where we'll be going to other nodes in our graph
      for (Flight outGoingFlight : this.adjacencyList.get(city)) {
        newItinerary = new Itinerary((ArrayList<Flight>) currentItinerary.getFlights().clone());
        newItinerary.addFlight(outGoingFlight);
        //System.out.println(outGoingFlight);
        if (!visited.contains(newItinerary)) {
          if (this.withinRestrictedLayoverTimes(currentItinerary.getLastFlight(), outGoingFlight)) {
            visited.add(newItinerary);
            queue.add(newItinerary);
          }
        } 
      }
    }
    return ret;
  }


  /**
   * Generates a list of Itinerary which includes all possible ways to go from one city being the
   * origin, to the ending city being the destination; the result is sorted by total cost. This
   * method creates an implicit graph from the Flights in our database and generates the Itineraries
   * by doing a breadth first search.
   * 
   * @param departureDate object specifying the time in which we will depart from
   * @param origin object specifying the starting city we are departing from
   * @param destination object specifying the ending city we are traveling to
   * @return list of Itinerary which we will use
   */
  public ArrayList<Itinerary> queryIteneraryByCost(String departureDate, String origin,
      String destination) {
    ArrayList<Itinerary> arrayListItineraries =
        this.generateAllItineraries(departureDate, origin, destination);
    Collections.sort(arrayListItineraries, new Comparator<Itinerary>() {
      @Override
      public int compare(Itinerary it1, Itinerary it2) {
        return it1.getTotalCost() < it2.getTotalCost() ? -1
            : (it1.getTotalCost() == it2.getTotalCost() ? 0 : 1);
      }
    });
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
  public ArrayList<Itinerary> queryIteneraryByTime(String departureDate, String origin,
      String destination) {
    ArrayList<Itinerary> arrayListItineraries =
        this.generateAllItineraries(departureDate, origin, destination);
    Collections.sort(arrayListItineraries, new Comparator<Itinerary>() {
      @Override
      public int compare(Itinerary it1, Itinerary it2) {
        return it1.getTotalTravelTime().compareTo(it2.getTotalTravelTime());
      }
    });
    return arrayListItineraries;
  }

  /**
   * Returns the absolute file path to the file containing our flight information. 
   * @return the savedFilepath
   */
  public String getSavedFilepath() {
    return savedFilepath;
  }

  /**
   * Determines if a flight is in line with our departure date and location.
   * 
   * @param flight flight to be checked
   * @param departureDate departure date in "YYYY-MM-DD" format
   * @param origin original location we are departing from
   * @return true if they're in line, false otherwise
   */
  private boolean isFlightStartingPoint(Flight flight, String departureDate, String origin) {
    String[] dates = departureDate.split("-");
    int year = Integer.parseInt(dates[0]);
    int month = Integer.parseInt(dates[1]);
    int days = Integer.parseInt(dates[2]);
    Calendar cal = Calendar.getInstance();
    cal.setTime(flight.getDepartureDateTime());
    return flight.getOrigin().equals(origin) && cal.get(Calendar.YEAR) == year
        && cal.get(Calendar.MONTH) + 1 == month && cal.get(Calendar.DAY_OF_MONTH) == days;
  }


  /**
   * Determines if two flights are within the {@link MIN_LAYOVER} and {@link MAX_LAYOVER}. 
   * @param flight1 flight preceding the second one
   * @param flight2 flight we're moving to
   * @return True if these flights are within the restrictions and flight1 occurs before flight2
   */
  private boolean withinRestrictedLayoverTimes(Flight flight1, Flight flight2) {
    if (flight1.getArrivalDateTime().before(flight2.getDepartureDateTime())) {
      Instant instance1 = flight1.getArrivalDateTime().toInstant();
      Instant instance2 = flight2.getDepartureDateTime().toInstant();
      Duration timeTaken = Duration.between(instance1, instance2);
      if (timeTaken.compareTo(MIN_LAYOVER) >= 0 && timeTaken.compareTo(MAX_LAYOVER) == -1) {
        return true;
      }
      return false;
    } 
    return false;
  }

  /**
   * Writes the current database to file.
   * @throws IOException the file was deleted or something
   */
  public void writeFlightDatabaseToFile() throws IOException {
    PrintWriter pw = new PrintWriter(new FileWriter(this.savedFilepath));
    for (Flight flight : this.getFlights()) {
      pw.println(flight.toString());
    }
    pw.close();
  }

  /**
   * Returns a list of flights that go from origin to destination directly on a certain date.
   * 
   * @param date given
   * @param origin starting point
   * @param destination ending point
   * @return List of lines in the Flight databases that satisfies these queries
   * @throws IOException exception
   */
  public List<String> getDirectFlights(String date, String origin, String destination)
      throws IOException {
    List<String> ret = new ArrayList<String>();
    for (Flight flight : this.flights) {
      DateFormat converter = new SimpleDateFormat("yyyy-MM-dd");
      String flightDay = converter.format(flight.getDepartureDateTime());
      System.out.println(flightDay);
      if (flightDay.equals(date)) {
        if (flight.getOrigin().equals(origin)) {
          if (flight.getDestination().equals(destination)) {
            ret.add(flight.toString());
            System.out.println(flight.toString());
          }
        }
      }
    }
    return ret;
  }

}

